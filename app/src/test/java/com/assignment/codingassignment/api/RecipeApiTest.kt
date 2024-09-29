package com.assignment.codingassignment.api

import com.assignment.codingassignment.BuildConfig
import com.assignment.codingassignment.UtilityTest
import com.assignment.codingassignment.UtilityTest.Companion.enqueueMockResponse
import com.assignment.codingassignment.UtilityTest.Companion.enqueueRecipeMockResponse
import com.assignment.codingassignment.network.RecipeService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(MockitoJUnitRunner::class)
class RecipeApiTest {
    private lateinit var service: RecipeService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = UtilityTest.getRecipeService()
    }

    @Test
    fun getSearch_receivedResponse_correctPageSize() {
        runBlocking {
            val recipeSearchResponse = enqueueRecipeMockResponse("recipe.json")

            // Call the API
            val responseBody =
                service.search(BuildConfig.APP_TOKEN, 3, "beef carrot potato onion").body()
            val recipesList = responseBody?.recipes

            assertThat(responseBody).isNotNull()
            assertThat(recipesList?.size).isEqualTo(recipeSearchResponse.count)
        }
    }

    @Test
    fun getSearch_sentRequest_receivedExpected() {
        runBlocking {
            val responseBody =
                service.search(BuildConfig.APP_TOKEN, 3, "beef carrot potato onion").body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("search/?page=3&query=beef%20carrot%20potato%20onion")
        }
    }


    @Test
    fun getSearch_receivedResponse_emptySize() {
        runBlocking {
            enqueueMockResponse("recipe.json")
            val responseBody =
                service.search("", 0, "").body()
            val recipesList = responseBody?.recipes
            assertThat(recipesList?.size).isEqualTo(null)
        }
    }

    @Test
    fun getSearch_receivedResponse_correctContent() {
        runBlocking {
            val recipeSearchResponse = enqueueRecipeMockResponse("recipe.json")
            val responseBody =
                service.search(BuildConfig.APP_TOKEN, 3, "beef carrot potato onion").body()
            val recipesList = responseBody!!.recipes
            val recipe = recipesList[0]
            assertThat(recipe.title).isEqualTo(recipeSearchResponse.recipes[0].title)
            assertThat(recipe.description).isEqualTo(recipeSearchResponse.recipes[0].description)
            assertThat(recipe.sourceUrl).isEqualTo(recipeSearchResponse.recipes[0].sourceUrl)
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}