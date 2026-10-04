package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BotanicalAnalysisResult

@Composable
fun ConfidenceBadge(confidence: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when {
        confidence.contains("high", ignoreCase = true) -> Triple(
            Color(0xFFD8F3DC),
            Color(0xFF1B4332),
            "High Confidence"
        )
        confidence.contains("low", ignoreCase = true) -> Triple(
            Color(0xFFFFE0B2),
            Color(0xFFE65100),
            "Low Confidence"
        )
        else -> Triple(
            Color(0xFFFFF3CD),
            Color(0xFF856404),
            "Medium Confidence"
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(textColor, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ToxicityWarningBanner(
    isToxic: Boolean,
    details: String = "",
    contraindications: String = "",
    modifier: Modifier = Modifier
) {
    if (isToxic) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .border(1.5.dp, Color(0xFFD32F2F), RoundedCornerShape(16.dp))
                .testTag("toxicity_warning_banner")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFD32F2F), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dangerous,
                            contentDescription = "Toxicity Hazard",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "CRITICAL TOXICITY WARNING",
                            color = Color(0xFFB71C1C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Hazardous to humans or domestic pets",
                            color = Color(0xFFC62828),
                            fontSize = 12.sp
                        )
                    }
                }

                if (details.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = details,
                        color = Color(0xFF3E0004),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                if (contraindications.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Contraindications: $contraindications",
                        color = Color(0xFF4A0E17),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    } else {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF81C784), RoundedCornerShape(16.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Safe Specimen",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "No Primary Ingestion Hazard Flagged",
                        color = Color(0xFF1B5E20),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (contraindications.isNotBlank()) contraindications else "Generally considered non-toxic in typical botanical usage.",
                        color = Color(0xFF2E7D32),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BioactiveCompoundsList(compounds: List<String>, raw: String = "", modifier: Modifier = Modifier) {
    val items = if (compounds.isNotEmpty()) compounds else raw.split(",", ";").map { it.trim() }.filter { it.isNotBlank() }

    if (items.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            items.forEach { compound ->
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = compound,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    } else if (raw.isNotBlank()) {
        Text(
            text = raw,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun BotanicalSectionCard(
    sectionNumber: Int,
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.5.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$sectionNumber",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun JsonExportCard(
    jsonString: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("json_export_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "6. Database JSON Export",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Botanical Plant JSON", jsonString)
                        clipboard.setPrimaryClip(clip)
                        copied = true
                        Toast.makeText(context, "JSON copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.testTag("copy_json_button")
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy JSON",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (copied) "Copied" else "Copy JSON", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = jsonString.ifBlank { "{}" },
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
