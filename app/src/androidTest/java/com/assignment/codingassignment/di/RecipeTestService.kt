package com.assignment.codingassignment.di

import com.assignment.codingassignment.network.responses.RecipeSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeTestService {
    @GET("search")
    open suspend fun search(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<RecipeSearchResponse>

}