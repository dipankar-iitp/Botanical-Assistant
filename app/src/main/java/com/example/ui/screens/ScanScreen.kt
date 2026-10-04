package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.sample.SamplePlants
import com.example.data.sample.SampleSpecimen
import com.example.ui.components.BioactiveCompoundsList
import com.example.ui.components.BotanicalSectionCard
import com.example.ui.components.ConfidenceBadge
import com.example.ui.components.JsonExportCard
import com.example.ui.components.ToxicityWarningBanner
import com.example.ui.viewmodel.BotanicalViewModel

@Composable
fun ScanScreen(
    viewModel: BotanicalViewModel,
    modifier: Modifier = Modifier
) {
    val selectedBitmap by viewModel.selectedImageBitmap.collectAsStateWithLifecycle()
    val userNotes by viewModel.userNotesInput.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val analysisStage by viewModel.analysisStage.collectAsStateWithLifecycle()
    val analysisError by viewModel.analysisError.collectAsStateWithLifecycle()
    val scanResult by viewModel.currentScanResult.collectAsStateWithLifecycle()

    var savedFeedback by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setImageFromUri(uri)
            savedFeedback = false
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.setImageBitmap(bitmap)
            savedFeedback = false
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("scan_screen_scroll"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Botanical Database Assistant",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Multimodal AI Botanist & Herbalist • Leaf, Flower & Toxicity Analyzer",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Image Selection Area
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Specimen Visual Input",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (selectedBitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                bitmap = selectedBitmap!!.asImageBitmap(),
                                contentDescription = "Selected Botanical Specimen",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { viewModel.clearSelectedImage() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .testTag("remove_image_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove Image",
                                    tint = Color.White
                                )
                            }
                        }
                    } else {
                        // Empty photo picker slot
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Capture or pick a plant photo",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Leaves, flowers, stems, or whole herb",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pick_photo_button")
                        ) {
                            Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick Photo")
                        }

                        OutlinedButton(
                            onClick = { cameraLauncher.launch(null) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("camera_button")
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Take Photo")
                        }
                    }
                }
            }
        }

        // Accompanying Field Notes / Habitat Context
        item {
            OutlinedTextField(
                value = userNotes,
                onValueChange = { viewModel.onUserNotesChanged(it) },
                label = { Text("Field Notes / Habitat Context (Optional)") },
                placeholder = { Text("e.g. Found in shady riverbank, serrated leaves, aromatic menthol smell...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("field_notes_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )
        }

        // Analyze Action Button
        item {
            Button(
                onClick = {
                    savedFeedback = false
                    viewModel.analyzeCurrentSpecimen()
                },
                enabled = !isAnalyzing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("analyze_button")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = analysisStage.ifBlank { "Analyzing Specimen..." },
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Icon(imageVector = Icons.Default.EnergySavingsLeaf, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyze Botanical Specimen", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Error message if any
        if (analysisError != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("analysis_error_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = analysisError!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Curated Sample Specimens for Instant Testing
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Curated Specimens (Instant AI Test)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Tap to load",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(SamplePlants.ALL) { specimen ->
                        SampleSpecimenChip(
                            specimen = specimen,
                            onClick = {
                                savedFeedback = false
                                viewModel.loadSampleSpecimen(specimen)
                            }
                        )
                    }
                }
            }
        }

        // Analysis Results (The 6 Structured Sections)
        if (scanResult != null) {
            val result = scanResult!!

            // Result Header Banner
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("result_header_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ConfidenceBadge(confidence = result.confidenceScore)

                            if (result.toxicityWarning) {
                                Surface(
                                    color = Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Dangerous,
                                            contentDescription = null,
                                            tint = Color(0xFFD32F2F),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "TOXIC HAZARD",
                                            color = Color(0xFFD32F2F),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = result.scientificName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = result.commonNames.joinToString(" • "),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Family: ${result.family}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section 1: Plant Identification
            item {
                BotanicalSectionCard(
                    sectionNumber = 1,
                    title = "Plant Identification",
                    icon = Icons.Default.LocalFlorist
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        InfoRow(label = "Common Name(s)", value = result.commonNames.joinToString(", "))
                        InfoRow(label = "Scientific Name", value = result.scientificName, italic = true)
                        InfoRow(label = "Botanical Family", value = result.family)
                        InfoRow(label = "Identification Confidence", value = "${result.confidenceScore} ${if (result.confidenceReason.isNotBlank()) "- " + result.confidenceReason else ""}")
                    }
                }
            }

            // Section 2: Visual Analysis
            item {
                BotanicalSectionCard(
                    sectionNumber = 2,
                    title = "Visual Analysis",
                    icon = Icons.Outlined.Visibility
                ) {
                    Column {
                        Text(
                            text = "Observed Features",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = result.observedFeatures.ifBlank { "Visual characteristics identified from specimen image." },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Section 3: Historical & Cultural Context
            item {
                BotanicalSectionCard(
                    sectionNumber = 3,
                    title = "Historical & Cultural Context",
                    icon = Icons.Outlined.HistoryEdu
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (result.origin.isNotBlank()) {
                            Column {
                                Text(
                                    text = "Origin / Native Region",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = result.origin,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (result.culturalSignificance.isNotBlank()) {
                            Column {
                                Text(
                                    text = "Cultural Significance & Folklore",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = result.culturalSignificance,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Section 4: Traditional Medicinal & Therapeutic Uses
            item {
                BotanicalSectionCard(
                    sectionNumber = 4,
                    title = "Traditional Medicinal & Therapeutic Uses",
                    icon = Icons.Default.LocalPharmacy
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (result.documentedUses.isNotBlank()) {
                            Column {
                                Text(
                                    text = "Documented Traditional Uses",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = result.documentedUses,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Primary Active Compounds",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BioactiveCompoundsList(
                                compounds = result.activeCompounds,
                                raw = result.activeCompoundsRaw
                            )
                        }
                    }
                }
            }

            // Section 5: Safety, Toxicity & Precautions (CRITICAL)
            item {
                BotanicalSectionCard(
                    sectionNumber = 5,
                    title = "Safety, Toxicity & Precautions (CRITICAL)",
                    icon = if (result.toxicityWarning) Icons.Default.Dangerous else Icons.Default.Security
                ) {
                    ToxicityWarningBanner(
                        isToxic = result.toxicityWarning,
                        details = result.toxicityDetails,
                        contraindications = result.contraindications
                    )
                }
            }

            // Section 6: Database JSON Export
            item {
                JsonExportCard(jsonString = result.jsonExport)
            }

            // Herbarium Action Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (savedFeedback) "Saved to Herbarium!" else "Specimen Recorded",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Stored in local Room database for offline reference and search.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.saveCurrentResultToHerbarium()
                                savedFeedback = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.testTag("save_herbarium_button")
                        ) {
                            Icon(imageVector = Icons.Default.BookmarkAdd, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (savedFeedback) "Saved" else "Save")
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SampleSpecimenChip(
    specimen: SampleSpecimen,
    onClick: () -> Unit
) {
    Surface(
        color = if (specimen.isToxic) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(
                1.dp,
                if (specimen.isToxic) Color(0xFFEF9A9A) else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(12.dp)
            )
            .testTag("sample_chip_${specimen.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = if (specimen.isToxic) Icons.Default.Dangerous else Icons.Default.LocalFlorist,
                contentDescription = null,
                tint = if (specimen.isToxic) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = specimen.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = if (specimen.isToxic) Color(0xFFB71C1C) else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = specimen.scientificName,
                    fontStyle = FontStyle.Italic,
                    fontSize = 11.sp,
                    color = if (specimen.isToxic) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, italic: Boolean = false) {
    if (value.isBlank()) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(130.dp)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            fontWeight = if (italic) FontWeight.Medium else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}
