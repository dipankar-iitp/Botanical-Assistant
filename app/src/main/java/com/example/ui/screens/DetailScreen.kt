package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlantEntity
import com.example.ui.components.BioactiveCompoundsList
import com.example.ui.components.BotanicalSectionCard
import com.example.ui.components.ConfidenceBadge
import com.example.ui.components.JsonExportCard
import com.example.ui.components.ToxicityWarningBanner
import com.example.ui.viewmodel.BotanicalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    plant: PlantEntity,
    viewModel: BotanicalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var notesText by remember(plant.id) { mutableStateOf(plant.userNotes) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dossier, 1: Safety & Uses, 2: JSON

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = plant.scientificName,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(plant) }) {
                        Icon(
                            imageVector = if (plant.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (plant.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Botanical Specimen", "${plant.scientificName} (${plant.commonNames})\n\n${plant.rawMarkdown}")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Full dossier copied!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Specimen")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Specimen Header
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ConfidenceBadge(confidence = plant.confidenceScore)

                            if (plant.toxicityWarning) {
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

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = plant.scientificName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = plant.commonNames,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Family: ${plant.family}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Tabs for organized viewing
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Botanical Dossier") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Safety & Uses") }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Database JSON") }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    // Section 1: Identification
                    item {
                        BotanicalSectionCard(
                            sectionNumber = 1,
                            title = "Plant Identification",
                            icon = Icons.Default.LocalFlorist
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                DetailRow("Scientific Name", plant.scientificName, italic = true)
                                DetailRow("Common Names", plant.commonNames)
                                DetailRow("Family", plant.family)
                                DetailRow("Confidence", "${plant.confidenceScore} ${if (plant.confidenceReason.isNotBlank()) "- " + plant.confidenceReason else ""}")
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
                            Text(
                                text = plant.observedFeatures.ifBlank { "Visual characteristics identified from specimen image." },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
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
                                if (plant.origin.isNotBlank()) {
                                    Column {
                                        Text(
                                            text = "Origin / Native Region",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = plant.origin,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                if (plant.culturalSignificance.isNotBlank()) {
                                    Column {
                                        Text(
                                            text = "Cultural Significance & Lore",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = plant.culturalSignificance,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Section 4: Traditional Medicinal & Therapeutic Uses
                    item {
                        BotanicalSectionCard(
                            sectionNumber = 4,
                            title = "Medicinal & Therapeutic Uses",
                            icon = Icons.Default.LocalPharmacy
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                if (plant.medicinalUses.isNotBlank()) {
                                    Column {
                                        Text(
                                            text = "Documented Uses",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = plant.medicinalUses,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "Active Bioactive Compounds",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    BioactiveCompoundsList(compounds = emptyList(), raw = plant.activeCompounds)
                                }
                            }
                        }
                    }

                    // Section 5: Safety, Toxicity & Precautions (CRITICAL)
                    item {
                        BotanicalSectionCard(
                            sectionNumber = 5,
                            title = "Safety, Toxicity & Precautions (CRITICAL)",
                            icon = if (plant.toxicityWarning) Icons.Default.Dangerous else Icons.Default.Security
                        ) {
                            ToxicityWarningBanner(
                                isToxic = plant.toxicityWarning,
                                details = plant.toxicityDetails,
                                contraindications = plant.contraindications
                            )
                        }
                    }
                }
                2 -> {
                    // Section 6: Database JSON Export
                    item {
                        JsonExportCard(jsonString = plant.jsonExport)
                    }
                }
            }

            // User Field Notes
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Personal Field Notes & Habitat Observations",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = notesText,
                            onValueChange = { notesText = it },
                            placeholder = { Text("Add soil conditions, coordinates, harvest dates...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.updatePlantNotes(plant, notesText)
                                Toast.makeText(context, "Notes saved!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Notes")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, italic: Boolean = false) {
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
