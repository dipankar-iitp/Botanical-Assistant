package com.example.data.model

object BotanicalMarkdownParser {

    fun parse(markdown: String): BotanicalAnalysisResult {
        if (markdown.isBlank()) return BotanicalAnalysisResult()

        var scientificName = ""
        val commonNames = mutableListOf<String>()
        var family = ""
        var confidenceScore = "Medium"
        var confidenceReason = ""
        var observedFeatures = ""
        var origin = ""
        var culturalSignificance = ""
        var documentedUses = ""
        val activeCompounds = mutableListOf<String>()
        var activeCompoundsRaw = ""
        var toxicityWarning = false
        var jsonHadToxicity: Boolean? = null
        var toxicityDetails = ""
        var contraindications = ""
        var jsonExport = ""

        // 1. Try to extract the JSON block first
        val jsonPattern = Regex("```(?:json)?\\s*([\\s\\S]*?)\\s*```", RegexOption.IGNORE_CASE)
        val jsonMatch = jsonPattern.find(markdown)
        if (jsonMatch != null) {
            val jsonStr = jsonMatch.groupValues[1].trim()
            jsonExport = jsonStr

            // Regex extraction works identically in Android OS, Robolectric, and host JVM unit tests
            val sciMatch = Regex("\"scientific_name\"\\s*:\\s*\"([^\"]+)\"").find(jsonStr)
            if (sciMatch != null) {
                scientificName = sciMatch.groupValues[1].trim()
            }
            val toxMatch = Regex("\"toxicity_warning\"\\s*:\\s*(true|false)", RegexOption.IGNORE_CASE).find(jsonStr)
            if (toxMatch != null) {
                toxicityWarning = toxMatch.groupValues[1].toBoolean()
                jsonHadToxicity = toxicityWarning
            }
            val confMatch = Regex("\"confidence_score\"\\s*:\\s*\"([^\"]+)\"").find(jsonStr)
            if (confMatch != null) {
                val conf = confMatch.groupValues[1].trim()
                confidenceScore = when {
                    conf.contains("high", ignoreCase = true) -> "High"
                    conf.contains("low", ignoreCase = true) -> "Low"
                    else -> "Medium"
                }
            }
            val commonMatch = Regex("\"common_names\"\\s*:\\s*\\[([^\\]]+)\\]").find(jsonStr)
            if (commonMatch != null) {
                val namesContent = commonMatch.groupValues[1]
                Regex("\"([^\"]+)\"").findAll(namesContent).forEach { match ->
                    val name = match.groupValues[1].trim()
                    if (name.isNotEmpty() && !commonNames.contains(name)) {
                        commonNames.add(name)
                    }
                }
            }
        }

        // 2. Parse markdown sections and bullet points
        val lines = markdown.lines()
        var currentSection = 0 // 1..6

        val section1Regex = Regex("(?:\\*\\*)?1\\.\\s*Plant Identification(?:\\*\\*)?", RegexOption.IGNORE_CASE)
        val section2Regex = Regex("(?:\\*\\*)?2\\.\\s*Visual Analysis(?:\\*\\*)?", RegexOption.IGNORE_CASE)
        val section3Regex = Regex("(?:\\*\\*)?3\\.\\s*Historical(?:\\s*&\\s*Cultural)?(?:\\*\\*)?", RegexOption.IGNORE_CASE)
        val section4Regex = Regex("(?:\\*\\*)?4\\.\\s*Traditional Medicinal(?:\\*\\*)?", RegexOption.IGNORE_CASE)
        val section5Regex = Regex("(?:\\*\\*)?5\\.\\s*Safety(?:\\s*,\\s*Toxicity)?(?:\\*\\*)?", RegexOption.IGNORE_CASE)
        val section6Regex = Regex("(?:\\*\\*)?6\\.\\s*Database JSON(?:\\*\\*)?", RegexOption.IGNORE_CASE)

        val section2Buffer = StringBuilder()
        val section3Buffer = StringBuilder()
        val section4Buffer = StringBuilder()
        val section5Buffer = StringBuilder()

        for (line in lines) {
            val trimmed = line.trim()

            when {
                section1Regex.containsMatchIn(trimmed) -> { currentSection = 1; continue }
                section2Regex.containsMatchIn(trimmed) -> { currentSection = 2; continue }
                section3Regex.containsMatchIn(trimmed) -> { currentSection = 3; continue }
                section4Regex.containsMatchIn(trimmed) -> { currentSection = 4; continue }
                section5Regex.containsMatchIn(trimmed) -> { currentSection = 5; continue }
                section6Regex.containsMatchIn(trimmed) -> { currentSection = 6; continue }
            }

            when (currentSection) {
                1 -> {
                    val lower = trimmed.lowercase()
                    when {
                        lower.contains("common name") -> {
                            val value = extractValueAfterColon(trimmed)
                            if (commonNames.isEmpty() && value.isNotBlank()) {
                                value.split(",", ";").map { cleanValue(it) }.filter { it.isNotBlank() }
                                    .forEach { if (!commonNames.contains(it)) commonNames.add(it) }
                            }
                        }
                        lower.contains("scientific name") -> {
                            if (scientificName.isBlank()) {
                                scientificName = cleanValue(extractValueAfterColon(trimmed))
                            }
                        }
                        lower.contains("family") -> {
                            if (family.isBlank()) {
                                family = cleanValue(extractValueAfterColon(trimmed))
                            }
                        }
                        lower.contains("confidence") -> {
                            val fullVal = extractValueAfterColon(trimmed)
                            val cleanVal = cleanValue(fullVal)
                            if (cleanVal.contains("high", ignoreCase = true)) confidenceScore = "High"
                            else if (cleanVal.contains("low", ignoreCase = true)) confidenceScore = "Low"
                            else confidenceScore = "Medium"
                            val dashSplit = cleanVal.split("-", ":", "—")
                            if (dashSplit.size > 1) {
                                confidenceReason = dashSplit.drop(1).joinToString("-").trim()
                            } else {
                                confidenceReason = cleanVal
                            }
                        }
                    }
                }
                2 -> {
                    if (trimmed.isNotBlank()) {
                        val lower = trimmed.lowercase()
                        if (lower.contains("observed features")) {
                            observedFeatures = cleanValue(extractValueAfterColon(trimmed))
                        } else {
                            if (observedFeatures.isBlank()) {
                                section2Buffer.append(cleanValue(trimmed)).append("\n")
                            } else {
                                section2Buffer.append(trimmed).append("\n")
                            }
                        }
                    }
                }
                3 -> {
                    if (trimmed.isNotBlank()) {
                        val lower = trimmed.lowercase()
                        if (lower.contains("origin") || lower.contains("native region")) {
                            origin = cleanValue(extractValueAfterColon(trimmed))
                        } else if (lower.contains("cultural significance")) {
                            culturalSignificance = cleanValue(extractValueAfterColon(trimmed))
                        } else {
                            section3Buffer.append(cleanValue(trimmed)).append(" ")
                        }
                    }
                }
                4 -> {
                    if (trimmed.isNotBlank()) {
                        val lower = trimmed.lowercase()
                        if (lower.contains("documented uses")) {
                            documentedUses = cleanValue(extractValueAfterColon(trimmed))
                        } else if (lower.contains("active compound")) {
                            activeCompoundsRaw = cleanValue(extractValueAfterColon(trimmed))
                            activeCompoundsRaw.split(",", ";").map { cleanValue(it) }.filter { it.isNotBlank() }
                                .forEach { if (!activeCompounds.contains(it)) activeCompounds.add(it) }
                        } else {
                            section4Buffer.append(cleanValue(trimmed)).append(" ")
                        }
                    }
                }
                5 -> {
                    if (trimmed.isNotBlank()) {
                        val lower = trimmed.lowercase()
                        if (lower.contains("toxic")) {
                            toxicityDetails = cleanValue(extractValueAfterColon(trimmed))
                            if (jsonHadToxicity == null) {
                                if (lower.contains("non-toxic") || lower.contains("not toxic") || lower.contains("non toxic")) {
                                    toxicityWarning = false
                                } else if (lower.contains("yes") || lower.contains("deadly") || lower.contains("poisonous") || lower.contains("danger") || lower.contains("fatal") || lower.contains("hazard") || (lower.contains("toxic") && !lower.contains("non-toxic") && !lower.contains("not toxic"))) {
                                    toxicityWarning = true
                                }
                            }
                        } else if (lower.contains("contraindication")) {
                            contraindications = cleanValue(extractValueAfterColon(trimmed))
                        } else {
                            section5Buffer.append(cleanValue(trimmed)).append(" ")
                        }
                    }
                }
            }
        }

        if (observedFeatures.isBlank() && section2Buffer.isNotBlank()) {
            observedFeatures = section2Buffer.toString().trim()
        }
        if (origin.isBlank() && section3Buffer.isNotBlank()) {
            origin = section3Buffer.toString().trim()
        }
        if (documentedUses.isBlank() && section4Buffer.isNotBlank()) {
            documentedUses = section4Buffer.toString().trim()
        }
        if (toxicityDetails.isBlank() && section5Buffer.isNotBlank()) {
            toxicityDetails = section5Buffer.toString().trim()
        }

        // If JSON export wasn't explicitly found in markdown, generate fallback json
        if (jsonExport.isBlank()) {
            jsonExport = """
            {
              "scientific_name": "${scientificName.escapeJson()}",
              "common_names": [${commonNames.joinToString(", ") { "\"${it.escapeJson()}\"" }}],
              "confidence_score": "$confidenceScore",
              "toxicity_warning": $toxicityWarning
            }
            """.trimIndent()
        }

        return BotanicalAnalysisResult(
            commonNames = commonNames,
            scientificName = scientificName,
            family = family,
            confidenceScore = confidenceScore,
            confidenceReason = confidenceReason,
            observedFeatures = observedFeatures,
            origin = origin,
            culturalSignificance = culturalSignificance,
            documentedUses = documentedUses,
            activeCompounds = activeCompounds,
            activeCompoundsRaw = activeCompoundsRaw,
            toxicityWarning = toxicityWarning,
            toxicityDetails = toxicityDetails,
            contraindications = contraindications,
            jsonExport = jsonExport,
            rawMarkdown = markdown
        )
    }

    private fun extractValueAfterColon(line: String): String {
        val colonIdx = line.indexOf(':')
        val text = if (colonIdx != -1 && colonIdx < line.length - 1) {
            line.substring(colonIdx + 1)
        } else {
            line
        }
        return cleanValue(text)
    }

    private fun cleanValue(text: String): String {
        return text.trim { it <= ' ' || it == '*' || it == '_' || it == '[' || it == ']' || it == '-' }
    }

    private fun String.escapeJson(): String {
        return this.replace("\"", "\\\"").replace("\n", " ")
    }
}
