package com.assignment.codingassignment

import com.assignment.codingassignment.network.RecipeService
import com.assignment.codingassignment.network.responses.RecipeSearchResponse
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UtilityTest {
    companion object {
        fun enqueueMockResponse(fileName: String): MockResponse {
            val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()

            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            return mockResponse
        }

        fun enqueueRecipeMockResponse(fileName: String): RecipeSearchResponse {
            val jsonString = javaClass.classLoader!!.getResourceAsStream(fileName)
                .bufferedReader().use { it.readText() }
            val gson = Gson()
            return gson.fromJson(jsonString, RecipeSearchResponse::class.java)

        }

        private lateinit var server: MockWebServer
        fun getRecipeService(): RecipeService {
            server = MockWebServer()
            return Retrofit.Builder()
                .baseUrl(server.url(BuildConfig.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecipeService::class.java)
        }
    }
}