package com.assignment.codingassignment.network

import com.assignment.codingassignment.network.responses.RecipeSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeService {
    @GET("search")
    open suspend fun search(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<RecipeSearchResponse>


    @GET("search")
    open suspend fun searchWithFlow(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse

}