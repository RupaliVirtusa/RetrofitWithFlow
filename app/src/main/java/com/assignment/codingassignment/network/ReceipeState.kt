package com.assignment.codingassignment.network

import com.assignment.codingassignment.network.responses.RecipeSearchResponse
import retrofit2.Response

/**
 * This class is for handling the success / error response from api
 */
sealed class RecipeListState {
    data class  RecipeListLoaded(val response: RecipeSearchResponse) : RecipeListState()
    data class Error(val message: String) : RecipeListState()
    object Loading : RecipeListState()
}
