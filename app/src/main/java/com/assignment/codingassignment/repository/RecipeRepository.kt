package com.assignment.codingassignment.repository

import androidx.lifecycle.MutableLiveData
import com.assignment.codingassignment.network.RecipeListState
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    suspend fun search(token: String, page: Int, query: String): MutableLiveData<RecipeListState>

    suspend fun searchWithFlow(token: String, page: Int, query: String): Flow<RecipeListState?>

}