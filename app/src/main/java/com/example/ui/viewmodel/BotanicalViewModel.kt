package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiBotanicalService
import com.example.data.database.BotanicalDatabase
import com.example.data.model.BotanicalAnalysisResult
import com.example.data.model.PlantEntity
import com.example.data.repository.PlantRepository
import com.example.data.sample.SamplePlants
import com.example.data.sample.SampleSpecimen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class BotanicalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    private val geminiService = GeminiBotanicalService()

    init {
        val db = BotanicalDatabase.getDatabase(application)
        repository = PlantRepository(db.plantDao())
        preloadSampleDataIfNeeded()
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val allPlants: StateFlow<List<PlantEntity>> = repository.allPlants
        .combine(_searchQuery) { plants, query ->
            if (query.isBlank()) plants
            else plants.filter {
                it.scientificName.contains(query, ignoreCase = true) ||
                it.commonNames.contains(query, ignoreCase = true) ||
                it.activeCompounds.contains(query, ignoreCase = true) ||
                it.family.contains(query, ignoreCase = true)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoritePlants: StateFlow<List<PlantEntity>> = repository.favoritePlants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val toxicPlants: StateFlow<List<PlantEntity>> = repository.toxicPlants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedImageBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedImageBitmap = _selectedImageBitmap.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<String?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _userNotesInput = MutableStateFlow("")
    val userNotesInput = _userNotesInput.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()

    private val _analysisStage = MutableStateFlow("")
    val analysisStage = _analysisStage.asStateFlow()

    private val _analysisError = MutableStateFlow<String?>(null)
    val analysisError = _analysisError.asStateFlow()

    private val _currentScanResult = MutableStateFlow<BotanicalAnalysisResult?>(null)
    val currentScanResult = _currentScanResult.asStateFlow()

    private val _selectedPlantDetail = MutableStateFlow<PlantEntity?>(null)
    val selectedPlantDetail = _selectedPlantDetail.asStateFlow()

    val isApiKeyConfigured: Boolean
        get() = geminiService.isApiKeyConfigured()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onUserNotesChanged(notes: String) {
        _userNotesInput.value = notes
    }

    fun setImageFromUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val context = getApplication<Application>()
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                _selectedImageBitmap.value = bitmap
                _selectedImageUri.value = uri.toString()
                _analysisError.value = null
            } catch (e: Exception) {
                _analysisError.value = "Failed to load image: ${e.localizedMessage}"
            }
        }
    }

    fun setImageBitmap(bitmap: Bitmap) {
        _selectedImageBitmap.value = bitmap
        _selectedImageUri.value = null
        _analysisError.value = null
    }

    fun clearSelectedImage() {
        _selectedImageBitmap.value = null
        _selectedImageUri.value = null
        _currentScanResult.value = null
        _analysisError.value = null
        _userNotesInput.value = ""
    }

    fun analyzeCurrentSpecimen() {
        val bitmap = _selectedImageBitmap.value
        val notes = _userNotesInput.value

        if (bitmap == null && notes.isBlank()) {
            _analysisError.value = "Please select or capture a plant image first, or describe your specimen."
            return
        }

        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisError.value = null
            _analysisStage.value = "Inspecting floral margins & venation..."

            if (!geminiService.isApiKeyConfigured()) {
                // If API key is not configured, give clear message and demo option
                delay(1200)
                _analysisStage.value = "Resolving botanical taxonomy..."
                delay(800)
                _isAnalyzing.value = false
                _analysisError.value = "GEMINI_API_KEY is not configured in Secrets. You can test live analysis by adding your key, or select any of the curated sample specimens below!"
                return@launch
            }

            // Staged progress updates for rich botanical feel
            val progressJob = launch {
                delay(900)
                _analysisStage.value = "Cross-referencing herbal active compounds..."
                delay(1200)
                _analysisStage.value = "Evaluating toxicity & contraindications..."
                delay(1200)
                _analysisStage.value = "Compiling botanical dossier & JSON schema..."
            }

            val result = geminiService.analyzePlant(bitmap, notes)
            progressJob.cancel()
            _isAnalyzing.value = false

            result.onSuccess { analysis ->
                _currentScanResult.value = analysis
                // Automatically save to local database
                val entity = analysis.toEntity(
                    imageUri = _selectedImageUri.value,
                    userNotes = notes
                )
                repository.insert(entity)
            }.onFailure { err ->
                _analysisError.value = err.message ?: "Failed to analyze specimen. Please try again."
            }
        }
    }

    fun loadSampleSpecimen(specimen: SampleSpecimen) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisError.value = null
            _analysisStage.value = "Loading ${specimen.title}..."
            delay(500)
            val analysis = specimen.toAnalysisResult()
            _currentScanResult.value = analysis
            _selectedImageBitmap.value = null
            _selectedImageUri.value = null
            _userNotesInput.value = "Curated Reference Specimen: ${specimen.title}"
            _isAnalyzing.value = false
        }
    }

    fun saveCurrentResultToHerbarium(customNotes: String? = null) {
        val current = _currentScanResult.value ?: return
        viewModelScope.launch {
            val entity = current.toEntity(
                imageUri = _selectedImageUri.value,
                userNotes = customNotes ?: _userNotesInput.value
            )
            repository.insert(entity)
        }
    }

    fun selectPlantDetail(plant: PlantEntity) {
        _selectedPlantDetail.value = plant
    }

    fun clearPlantDetail() {
        _selectedPlantDetail.value = null
    }

    fun toggleFavorite(plant: PlantEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(plant)
            if (_selectedPlantDetail.value?.id == plant.id) {
                _selectedPlantDetail.value = plant.copy(isFavorite = !plant.isFavorite)
            }
        }
    }

    fun deletePlant(plant: PlantEntity) {
        viewModelScope.launch {
            repository.delete(plant)
            if (_selectedPlantDetail.value?.id == plant.id) {
                _selectedPlantDetail.value = null
            }
        }
    }

    fun updatePlantNotes(plant: PlantEntity, notes: String) {
        viewModelScope.launch {
            val updated = plant.copy(userNotes = notes)
            repository.update(updated)
            if (_selectedPlantDetail.value?.id == plant.id) {
                _selectedPlantDetail.value = updated
            }
        }
    }

    private fun preloadSampleDataIfNeeded() {
        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.getCount()
            if (count == 0) {
                for (specimen in SamplePlants.ALL) {
                    val analysis = specimen.toAnalysisResult()
                    val entity = analysis.toEntity(
                        imageUri = null,
                        userNotes = "Preloaded Reference Specimen"
                    )
                    repository.insert(entity)
                }
            }
        }
    }
}
