package com.example.data.api

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.data.model.BotanicalAnalysisResult
import com.example.data.model.BotanicalMarkdownParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GeminiBotanicalService {

    companion object {
        private const val TAG = "GeminiBotanicalService"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"
        private const val PRIMARY_MODEL = "gemini-3.5-flash"
        private const val FALLBACK_MODEL = "gemini-2.5-flash"

        private const val SYSTEM_INSTRUCTION = """You are the Multimodal Botanical Database Assistant, an expert AI botanist and herbalist. Your core objective is to analyze images of flora, medicinal plants, and herbs provided by the user, and deliver highly accurate, structured, and educational information about them.

When a user uploads an image (with or without accompanying text), you must analyze the visual characteristics (leaves, stems, flowers, fruit, environment) and provide a detailed breakdown of the plant.

Always structure your response using the following Markdown format. If you cannot identify the plant with high confidence, state your uncertainty clearly in the "Identification Confidence" section and suggest the closest possible matches.

### Output Format:

**1. Plant Identification**
* **Common Name(s):** [List common names, including regional names if applicable]
* **Scientific Name:** [Genus and species]
* **Family:** [Botanical family]
* **Identification Confidence:** [High / Medium / Low - brief reason why based on visual evidence]

**2. Visual Analysis**
* **Observed Features:** [Describe the key visual markers from the image, e.g., "serrated leaf edges," "purple bell-shaped flowers"]

**3. Historical & Cultural Context**
* **Origin/Native Region:** [Where is the plant natively found?]
* **Cultural Significance:** [Brief historical context or traditional uses in different cultures]

**4. Traditional Medicinal & Therapeutic Uses**
* **Documented Uses:** [What has this plant traditionally been used to treat or support?]
* **Active Compounds:** [List known primary active chemical compounds, if any]

**5. Safety, Toxicity & Precautions (CRITICAL)**
* **Toxicity:** [Is it toxic to humans or pets? List any dangerous look-alikes]
* **Contraindications:** [Who should avoid this? e.g., pregnant women, people on specific medications]

**6. Database JSON Export**
Provide a clean, valid JSON object of the core data so it can be routed to the frontend database. Use this exact schema:
```json
{
  "scientific_name": "",
  "common_names": [],
  "confidence_score": "",
  "toxicity_warning": true/false
}
```"""
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun analyzePlant(
        bitmap: Bitmap?,
        userNotes: String? = null
    ): Result<BotanicalAnalysisResult> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyConfigured()) {
            return@withContext Result.failure(
                IllegalStateException("GEMINI_API_KEY is not configured yet. Please add your key in the AI Studio Secrets panel.")
            )
        }

        try {
            // First attempt with primary model, fallback if needed
            val rawMarkdown = executeRequest(apiKey, PRIMARY_MODEL, bitmap, userNotes)
                ?: executeRequest(apiKey, FALLBACK_MODEL, bitmap, userNotes)
                ?: throw IllegalStateException("Empty response received from Botanical Assistant model.")

            val parsed = BotanicalMarkdownParser.parse(rawMarkdown)
            Result.success(parsed)
        } catch (e: Exception) {
            Log.e(TAG, "Error during plant analysis", e)
            Result.failure(e)
        }
    }

    private fun executeRequest(
        apiKey: String,
        model: String,
        bitmap: Bitmap?,
        userNotes: String?
    ): String? {
        val url = "$BASE_URL$model:generateContent?key=$apiKey"

        val contentsArray = JSONArray()
        val contentObj = JSONObject()
        val partsArray = JSONArray()

        // Prompt text
        val promptText = if (!userNotes.isNullOrBlank()) {
            "Please analyze this botanical specimen in detail. Additional field notes from user: $userNotes"
        } else {
            "Please identify and analyze this botanical specimen in detail following the strict 6-step botanical output format."
        }

        val textPart = JSONObject().put("text", promptText)
        partsArray.put(textPart)

        // Image part if provided
        if (bitmap != null) {
            val base64Data = bitmap.toBase64()
            val inlineData = JSONObject()
                .put("mimeType", "image/jpeg")
                .put("data", base64Data)
            val imagePart = JSONObject().put("inlineData", inlineData)
            partsArray.put(imagePart)
        }

        contentObj.put("parts", partsArray)
        contentsArray.put(contentObj)

        val requestJson = JSONObject()
        requestJson.put("contents", contentsArray)

        // System Instruction
        val sysInstructionObj = JSONObject()
        val sysPartsArray = JSONArray().put(JSONObject().put("text", SYSTEM_INSTRUCTION))
        sysInstructionObj.put("parts", sysPartsArray)
        requestJson.put("systemInstruction", sysInstructionObj)

        // Generation Config
        val genConfig = JSONObject()
            .put("temperature", 0.3)
            .put("topP", 0.95)
            .put("topK", 40)
        requestJson.put("generationConfig", genConfig)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = requestJson.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            val responseString = response.body?.string() ?: return null
            if (!response.isSuccessful) {
                Log.w(TAG, "Model $model returned error code: ${response.code}, message: $responseString")
                return null
            }

            val jsonResponse = JSONObject(responseString)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val stringBuilder = StringBuilder()
                    for (i in 0 until parts.length()) {
                        val part = parts.getJSONObject(i)
                        stringBuilder.append(part.optString("text", ""))
                    }
                    return stringBuilder.toString()
                }
            }
            return null
        }
    }

    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        // Resize if excessively large to keep request fast and within network bounds
        val scaled = if (width > 1200 || height > 1200) {
            val scale = 1200f / maxOf(width, height)
            Bitmap.createScaledBitmap(this, (width * scale).toInt(), (height * scale).toInt(), true)
        } else {
            this
        }
        scaled.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}
