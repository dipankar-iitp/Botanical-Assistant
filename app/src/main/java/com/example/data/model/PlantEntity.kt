package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val scientificName: String,
    val commonNames: String,
    val family: String,
    val confidenceScore: String, // High, Medium, Low
    val confidenceReason: String = "",
    val observedFeatures: String = "",
    val origin: String = "",
    val culturalSignificance: String = "",
    val medicinalUses: String = "",
    val activeCompounds: String = "",
    val toxicityWarning: Boolean = false,
    val toxicityDetails: String = "",
    val contraindications: String = "",
    val rawMarkdown: String = "",
    val jsonExport: String = "",
    val imageUri: String? = null,
    val userNotes: String = "",
    val isFavorite: Boolean = false
)
