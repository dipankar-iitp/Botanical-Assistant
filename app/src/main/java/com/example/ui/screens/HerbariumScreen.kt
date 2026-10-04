package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.PlantEntity
import com.example.ui.components.ConfidenceBadge
import com.example.ui.viewmodel.BotanicalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HerbariumScreen(
    viewModel: BotanicalViewModel,
    onPlantClick: (PlantEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val allPlants by viewModel.allPlants.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var selectedFilter by remember { mutableStateOf("All") } // "All", "Favorites", "Toxic", "Safe"
    var plantToDelete by remember { mutableStateOf<PlantEntity?>(null) }

    val filteredPlants = remember(allPlants, selectedFilter) {
        when (selectedFilter) {
            "Favorites" -> allPlants.filter { it.isFavorite }
            "Toxic" -> allPlants.filter { it.toxicityWarning }
            "Safe" -> allPlants.filter { !it.toxicityWarning }
            else -> allPlants
        }
    }

    if (plantToDelete != null) {
        AlertDialog(
            onDismissRequest = { plantToDelete = null },
            title = { Text("Delete Botanical Record?") },
            text = { Text("Are you sure you want to remove '${plantToDelete!!.scientificName}' from your herbarium?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePlant(plantToDelete!!)
                        plantToDelete = null
                    },
                    modifier = Modifier.testTag("confirm_delete_button")
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { plantToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("herbarium_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            placeholder = { Text("Search by scientific name, herb, compound...") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("herbarium_search_input"),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter Chips Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("All", "Favorites", "Toxic", "Safe").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 12.sp) },
                    modifier = Modifier.testTag("filter_chip_$filter")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredPlants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "No matching botanical records" else "Herbarium is empty",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Scan plants or explore curated specimens in the Scan tab to build your log.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.fillMaxSize().testTag("plants_lazy_column")
            ) {
                items(filteredPlants, key = { it.id }) { plant ->
                    HerbariumPlantCard(
                        plant = plant,
                        onClick = { onPlantClick(plant) },
                        onToggleFavorite = { viewModel.toggleFavorite(plant) },
                        onDelete = { plantToDelete = plant }
                    )
                }
            }
        }
    }
}

@Composable
fun HerbariumPlantCard(
    plant: PlantEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    val dateStr = remember(plant.timestamp) { dateFormat.format(Date(plant.timestamp)) }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("plant_card_${plant.id}")
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (plant.toxicityWarning) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.primaryContainer,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (plant.toxicityWarning) Icons.Default.Dangerous else Icons.Default.LocalFlorist,
                    contentDescription = null,
                    tint = if (plant.toxicityWarning) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plant.scientificName,
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (plant.toxicityWarning) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFFFCDD2),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "TOXIC",
                                color = Color(0xFFB71C1C),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = plant.commonNames,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plant.family,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateStr,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Row {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (plant.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (plant.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
