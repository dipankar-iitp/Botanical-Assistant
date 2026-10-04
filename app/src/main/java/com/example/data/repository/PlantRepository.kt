package com.example.data.repository

import com.example.data.database.PlantDao
import com.example.data.model.PlantEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlantRepository(private val plantDao: PlantDao) {

    val allPlants: Flow<List<PlantEntity>> = plantDao.getAllPlants()
    val favoritePlants: Flow<List<PlantEntity>> = plantDao.getFavoritePlants()
    val toxicPlants: Flow<List<PlantEntity>> = plantDao.getToxicPlants()

    fun getPlantById(id: Long): Flow<PlantEntity?> = plantDao.getPlantById(id)

    fun searchPlants(query: String): Flow<List<PlantEntity>> = plantDao.searchPlants(query)

    suspend fun insert(plant: PlantEntity): Long = withContext(Dispatchers.IO) {
        plantDao.insertPlant(plant)
    }

    suspend fun update(plant: PlantEntity) = withContext(Dispatchers.IO) {
        plantDao.updatePlant(plant)
    }

    suspend fun toggleFavorite(plant: PlantEntity) = withContext(Dispatchers.IO) {
        plantDao.updatePlant(plant.copy(isFavorite = !plant.isFavorite))
    }

    suspend fun updateNotes(plantId: Long, notes: String, plant: PlantEntity) = withContext(Dispatchers.IO) {
        plantDao.updatePlant(plant.copy(userNotes = notes))
    }

    suspend fun delete(plant: PlantEntity) = withContext(Dispatchers.IO) {
        plantDao.deletePlant(plant)
    }

    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        plantDao.deletePlantById(id)
    }

    suspend fun getCount(): Int = withContext(Dispatchers.IO) {
        plantDao.count()
    }
}
