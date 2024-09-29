package com.assignment.codingassignment.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.assignment.codingassignment.BuildConfig
import com.assignment.codingassignment.UtilityTest
import com.assignment.codingassignment.network.RecipeListState
import com.assignment.codingassignment.network.RecipeService
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
class RecipeRepositoryTest {

    private lateinit var repository: RecipeRepository

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repository = RecipeRepositoryImpl(UtilityTest.getRecipeService())
    }

    @Test
    fun getSearch_receivedResponse_correctPageSize() {
        runBlocking {
            val recipeSearchResponse = UtilityTest.enqueueRecipeMockResponse("recipe.json")
            val data = repository.search(BuildConfig.APP_TOKEN, 3, "beef carrot potato onion")

            data.value.let { recipeListState ->
                when (recipeListState) {
                    is RecipeListState.RecipeListLoaded -> {
                        assertThat(recipeListState.response).isNotNull()
                        assertThat(recipeListState.response.recipes.size)
                            .isEqualTo(recipeSearchResponse.count)
                    }

                    else -> {
                    }
                }
            }
        }
    }

    @Test
    fun getSearch_receivedResponseWithError() {
        runBlocking {
            val data = repository.search("", 3, "beef carrot potato onion")

            data.value.let { recipeListState ->
                when (recipeListState) {
                    is RecipeListState.Error -> {
                        assertThat(recipeListState.message).isNotNull()
                    }

                    else -> {
                    }
                }
            }
        }
    }

}