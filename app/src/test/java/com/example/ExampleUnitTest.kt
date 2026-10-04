package com.example

import com.example.data.model.BotanicalMarkdownParser
import com.example.data.sample.SamplePlants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun parseEchinaceaSample() {
        val result = BotanicalMarkdownParser.parse(SamplePlants.ECHINACEA.fullMarkdown)
        assertEquals("Echinacea purpurea", result.scientificName)
        assertTrue(result.commonNames.contains("Purple Coneflower"))
        assertFalse(result.toxicityWarning)
        assertEquals("High", result.confidenceScore)
    }

    @Test
    fun parseFoxgloveToxicity() {
        val result = BotanicalMarkdownParser.parse(SamplePlants.FOXGLOVE.fullMarkdown)
        assertEquals("Digitalis purpurea", result.scientificName)
        assertTrue(result.toxicityWarning)
        assertTrue(result.activeCompounds.any { it.contains("Digoxin", ignoreCase = true) })
    }

    @Test
    fun parseDeadlyNightshadeToxicity() {
        val result = BotanicalMarkdownParser.parse(SamplePlants.DEADLY_NIGHTSHADE.fullMarkdown)
        assertEquals("Atropa belladonna", result.scientificName)
        assertTrue(result.toxicityWarning)
    }
}
