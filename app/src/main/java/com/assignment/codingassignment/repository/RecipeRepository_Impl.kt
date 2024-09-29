package com.assignment.codingassignment.repository

import androidx.lifecycle.MutableLiveData
import com.assignment.codingassignment.network.RecipeListState
import com.assignment.codingassignment.network.RecipeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RecipeRepositoryImpl(
    private val recipeService: RecipeService,
) : RecipeRepository {
    override suspend fun search(
        token: String,
        page: Int,
        query: String
    ): MutableLiveData<RecipeListState> {
        val recipeState = MutableLiveData<RecipeListState>()
        recipeState.value = RecipeListState.Loading
        val response = recipeService.search(token, page, query)
        if (response.isSuccessful) {
            recipeState.value = response.body()?.let { RecipeListState.RecipeListLoaded(it) }
        } else {
            recipeState.value = RecipeListState.Error(response.errorBody().toString())
        }
        return recipeState
    }

    override suspend fun searchWithFlow(
        token: String,
        page: Int,
        query: String
    ): Flow<RecipeListState?> {
        return flow {
            emit(RecipeListState.Loading)
            val response = recipeService.searchWithFlow(token, page, query)
            emit(RecipeListState.RecipeListLoaded(response))
        }.flowOn(Dispatchers.IO)
            .catch {
                emit(RecipeListState.Error(it.message.toString()))
            }
    }
}