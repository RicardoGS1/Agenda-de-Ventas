package com.virtualworld.agendadeventas.core.ai

import com.virtualworld.agendadeventas.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiApiService @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateAnalysis(
        systemInstruction: String,
        userPrompt: String,
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("Gemini API key is not configured"))
        }

        try {
            val requestBody = JSONObject().apply {
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
                })
                put("contents", JSONArray().put(
                    JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
                    },
                ))
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.3)
                    put("maxOutputTokens", MAX_OUTPUT_TOKENS)
                    put("thinkingConfig", JSONObject().apply {
                        put("thinkingBudget", 0)
                    })
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL/models/$MODEL:generateContent")
                .addHeader("x-goog-api-key", apiKey)
                .addHeader("Content-Type", "application/json")
                .post(requestBody.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            client.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        IllegalStateException("Gemini API error ${response.code}: $body"),
                    )
                }
                Result.success(parseResponseText(body))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseResponseText(body: String): String {
        val json = JSONObject(body)
        if (json.has("error")) {
            val error = json.getJSONObject("error")
            throw IllegalStateException(error.optString("message", "Unknown Gemini error"))
        }
        val candidates = json.optJSONArray("candidates") ?: throw IllegalStateException("No analysis result")
        if (candidates.length() == 0) {
            throw IllegalStateException("Empty analysis result")
        }
        val content = candidates.getJSONObject(0).optJSONObject("content")
            ?: throw IllegalStateException("Invalid analysis response")
        val parts = content.optJSONArray("parts") ?: throw IllegalStateException("Invalid analysis response")
        if (parts.length() == 0) {
            throw IllegalStateException("Empty analysis text")
        }
        return limitResponseLength(parts.getJSONObject(0).getString("text").trim())
    }

    private fun limitResponseLength(text: String, maxChars: Int = MAX_RESPONSE_CHARS): String {
        if (text.length <= maxChars) return text
        val slice = text.take(maxChars)
        val lastSentenceEnd = maxOf(
            slice.lastIndexOf('.'),
            slice.lastIndexOf('!'),
            slice.lastIndexOf('?'),
            slice.lastIndexOf('\n'),
        )
        return if (lastSentenceEnd > maxChars * 0.6) {
            slice.take(lastSentenceEnd + 1).trim()
        } else {
            slice.trimEnd() + "…"
        }
    }

    companion object {
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"
        private const val MODEL = "gemini-2.5-flash"
        private const val MAX_OUTPUT_TOKENS = 900
        private const val MAX_RESPONSE_CHARS = 4000
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }
}
