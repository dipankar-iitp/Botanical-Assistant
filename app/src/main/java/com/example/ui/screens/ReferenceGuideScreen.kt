package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LookalikePair(
    val title: String,
    val toxicPlant: String,
    val toxicScientific: String,
    val safePlant: String,
    val safeScientific: String,
    val criticalDistinction: String,
    val dangerLevel: String
)

private val LOOKALIKE_PAIRS = listOf(
    LookalikePair(
        title = "Poison Hemlock vs. Queen Anne's Lace",
        toxicPlant = "Poison Hemlock (FATAL)",
        toxicScientific = "Conium maculatum",
        safePlant = "Queen Anne's Lace (Wild Carrot)",
        safeScientific = "Daucus carota",
        criticalDistinction = "Hemlock stems are smooth, hairless, and marked with distinctive purple splotches. Queen Anne's Lace stems are hairy, solid green, and usually have a single purple floret in the center of the umbel.",
        dangerLevel = "EXTREME FATAL RISK"
    ),
    LookalikePair(
        title = "Foxglove vs. Comfrey",
        toxicPlant = "Foxglove (LETHAL CARDIAC TOXIN)",
        toxicScientific = "Digitalis purpurea",
        safePlant = "Comfrey",
        safeScientific = "Symphytum officinale",
        criticalDistinction = "Basal first-year leaves look nearly identical. Foxglove leaves have distinctly toothed/scalloped margins and winged petioles. Comfrey leaves have smooth margins and decurrent wings running down the stem with prickly hairs.",
        dangerLevel = "LETHAL ARRHYTHMIA"
    ),
    LookalikePair(
        title = "Deadly Nightshade vs. Edible Berries",
        toxicPlant = "Deadly Nightshade",
        toxicScientific = "Atropa belladonna",
        safePlant = "Black Nightshade / Blueberries",
        safeScientific = "Solanum nigrum / Vaccinium",
        criticalDistinction = "Belladonna berries grow singly, glossy, seated in a large 5-pointed star calyx. Black Nightshade berries grow in small drooping umbrella clusters. Vaccinium has a circular crown ring.",
        dangerLevel = "LETHAL TOXIN"
    )
)

@Composable
fun ReferenceGuideScreen(
    modifier: Modifier = Modifier
) {
    var expandedPairIndex by remember { mutableStateOf<Int?>(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("reference_guide_screen"),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Safety Emergency Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFEF9A9A), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFD32F2F), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Botanical Toxicity Advisory",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFFB71C1C)
                        )
                        Text(
                            text = "Never ingest wild flora without 100% verified botanical identification. In case of emergency or accidental ingestion, contact Poison Control immediately.",
                            fontSize = 12.sp,
                            color = Color(0xFFC62828),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Section Title: Critical Lookalikes
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dangerous Lookalike Warnings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Lookalike Cards
        items(LOOKALIKE_PAIRS.indices.toList()) { index ->
            val pair = LOOKALIKE_PAIRS[index]
            val isExpanded = expandedPairIndex == index

            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedPairIndex = if (isExpanded) null else index }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = Color(0xFFFFCDD2),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = pair.dangerLevel,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB71C1C),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pair.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Hazardous:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F)
                                    )
                                    Text(
                                        text = pair.toxicPlant,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = Color(0xFFB71C1C)
                                    )
                                    Text(
                                        text = pair.toxicScientific,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 12.sp,
                                        color = Color(0xFFC62828)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Safe Lookalike:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    Text(
                                        text = pair.safePlant,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = pair.safeScientific,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 12.sp,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Diagnostic Key / Critical Distinction:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = pair.criticalDistinction,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Botanical Taxonomy Basics
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Botanist's Morphology Glossary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlossaryItem(
                        term = "Leaf Venation",
                        description = "Pattern of veins. Reticulate (net-like) in dicots; parallel in monocots; dichotomous (forking) in Ginkgo."
                    )
                    GlossaryItem(
                        term = "Leaf Margins",
                        description = "Entire (smooth), Serrate (saw-toothed forward), Dentate (toothed outward), Crenate (scalloped rounded teeth)."
                    )
                    GlossaryItem(
                        term = "Stem Morphology",
                        description = "Quadrangular (square cross-section) in Lamiaceae mint family; hollow vs. pith-filled in Apiaceae."
                    )
                    GlossaryItem(
                        term = "Active Phytochemicals",
                        description = "Terpenes, Flavonoids, Alkaloids, Cardiac Glycosides, Tannins, and Polysaccharides driving therapeutic and toxic actions."
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun GlossaryItem(term: String, description: String) {
    Column {
        Text(
            text = term,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.sp
        )
    }
}
