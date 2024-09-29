package com.assignment.codingassignment.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.assignment.codingassignment.BuildConfig
import com.assignment.codingassignment.network.RecipeListState
import com.assignment.codingassignment.network.RecipeService
import com.assignment.codingassignment.network.model.RecipeDto
import com.assignment.codingassignment.network.responses.RecipeSearchResponse
import com.assignment.codingassignment.presentation.RecipeListViewModel
import com.assignment.codingassignment.repository.RecipeRepository
import com.assignment.codingassignment.repository.RecipeRepositoryImpl
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
@HiltAndroidTest
class RecipeViewModelTest {

    lateinit var repository: RecipeRepository
    private lateinit var service: RecipeService

    @Mock
    @BindValue
    lateinit var context: Context

    @Mock
    lateinit var observer: Observer<List<RecipeDto>>

    private lateinit var viewModel: RecipeListViewModel
    private lateinit var server: MockWebServer

    @Before
    fun setup() {

        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(BuildConfig.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeService::class.java)
        repository = RecipeRepositoryImpl(service)
        viewModel = RecipeListViewModel(repository, BuildConfig.APP_TOKEN, context as Application)
    }

    @Test
    fun fetchData_returnsExpectedData() = runBlocking {
        val expectedData = MutableLiveData<RecipeListState>()

        expectedData.value = RecipeListState.RecipeListLoaded(
            RecipeSearchResponse(
                count = 10,
                recipes = listOf()
            )
        )
        Mockito.`when`(
            repository.search(
                "Constants.APP_TOKEN",
                1,
                "Chicken"
            )
        ).thenReturn(expectedData)

        viewModel.getAllRecipes()
        verify(observer).onChanged((expectedData.value as RecipeListState.RecipeListLoaded).response.recipes)
    }

}