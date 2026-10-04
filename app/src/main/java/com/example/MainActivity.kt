package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.HerbariumScreen
import com.example.ui.screens.ReferenceGuideScreen
import com.example.ui.screens.ScanScreen
import com.example.ui.theme.BotanicalAssistantTheme
import com.example.ui.viewmodel.BotanicalViewModel

enum class BotanicalNavTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    SCAN("Scan & Identify", Icons.Default.EnergySavingsLeaf),
    HERBARIUM("Herbarium", Icons.Default.LocalFlorist),
    GUIDE("Field Guide", Icons.AutoMirrored.Filled.MenuBook)
}

class MainActivity : ComponentActivity() {

    private val viewModel: BotanicalViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BotanicalAssistantTheme {
                val selectedDetailPlant by viewModel.selectedPlantDetail.collectAsStateWithLifecycle()
                var currentTab by remember { mutableStateOf(BotanicalNavTab.SCAN) }
                var showApiKeyInfoDialog by remember { mutableStateOf(false) }

                if (showApiKeyInfoDialog) {
                    AlertDialog(
                        onDismissRequest = { showApiKeyInfoDialog = false },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        title = { Text("Botanical AI Configuration") },
                        text = {
                            Column {
                                Text(
                                    text = if (viewModel.isApiKeyConfigured)
                                        "Gemini API key is configured! You can scan real-world flora via photo upload."
                                    else
                                        "To enable live custom camera identification, configure GEMINI_API_KEY in the AI Studio Secrets panel.\n\nEven without a key, you can explore, test, and analyze all curated specimens (Echinacea, Foxglove, Peppermint, Nightshade, St. John's Wort, Ginkgo) with complete botanical breakdowns!",
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showApiKeyInfoDialog = false }) {
                                Text("Got it")
                            }
                        }
                    )
                }

                if (selectedDetailPlant != null) {
                    DetailScreen(
                        plant = selectedDetailPlant!!,
                        viewModel = viewModel,
                        onBack = { viewModel.clearPlantDetail() }
                    )
                } else {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Spa,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Botanical Assistant",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(
                                        onClick = { showApiKeyInfoDialog = true },
                                        modifier = Modifier.testTag("api_info_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "API Status",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 3.dp,
                                modifier = Modifier.testTag("bottom_nav_bar")
                            ) {
                                BotanicalNavTab.values().forEach { tab ->
                                    NavigationBarItem(
                                        selected = currentTab == tab,
                                        onClick = { currentTab = tab },
                                        icon = {
                                            Icon(imageVector = tab.icon, contentDescription = tab.title)
                                        },
                                        label = {
                                            Text(
                                                text = tab.title,
                                                fontSize = 11.sp,
                                                fontWeight = if (currentTab == tab) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            AnimatedContent(
                                targetState = currentTab,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "nav_transition"
                            ) { targetTab ->
                                when (targetTab) {
                                    BotanicalNavTab.SCAN -> ScanScreen(
                                        viewModel = viewModel
                                    )
                                    BotanicalNavTab.HERBARIUM -> HerbariumScreen(
                                        viewModel = viewModel,
                                        onPlantClick = { plant ->
                                            viewModel.selectPlantDetail(plant)
                                        }
                                    )
                                    BotanicalNavTab.GUIDE -> ReferenceGuideScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
