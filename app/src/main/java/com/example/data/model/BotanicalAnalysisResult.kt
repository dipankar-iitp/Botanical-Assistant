package com.example.data.model

data class BotanicalAnalysisResult(
    val commonNames: List<String> = emptyList(),
    val scientificName: String = "Unknown Plant",
    val family: String = "Unknown Family",
    val confidenceScore: String = "Medium", // High, Medium, Low
    val confidenceReason: String = "",
    val observedFeatures: String = "",
    val origin: String = "",
    val culturalSignificance: String = "",
    val documentedUses: String = "",
    val activeCompounds: List<String> = emptyList(),
    val activeCompoundsRaw: String = "",
    val toxicityWarning: Boolean = false,
    val toxicityDetails: String = "",
    val contraindications: String = "",
    val jsonExport: String = "",
    val rawMarkdown: String = ""
) {
    fun toEntity(imageUri: String? = null, userNotes: String = ""): PlantEntity {
        return PlantEntity(
            scientificName = scientificName.ifBlank { "Unknown Specimen" },
            commonNames = commonNames.joinToString(", ").ifBlank { "Unidentified Flora" },
            family = family.ifBlank { "Botanical Specimen" },
            confidenceScore = confidenceScore,
            confidenceReason = confidenceReason,
            observedFeatures = observedFeatures,
            origin = origin,
            culturalSignificance = culturalSignificance,
            medicinalUses = documentedUses,
            activeCompounds = if (activeCompounds.isNotEmpty()) activeCompounds.joinToString(", ") else activeCompoundsRaw,
            toxicityWarning = toxicityWarning,
            toxicityDetails = toxicityDetails,
            contraindications = contraindications,
            rawMarkdown = rawMarkdown,
            jsonExport = jsonExport,
            imageUri = imageUri,
            userNotes = userNotes
        )
    }
}
