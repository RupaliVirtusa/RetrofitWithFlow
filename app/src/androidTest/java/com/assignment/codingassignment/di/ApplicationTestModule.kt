package com.assignment.codingassignment.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationTestModule {

    @Singleton
    @Provides
    fun provideRecipeService(): RecipeTestService {
        return Retrofit.Builder().baseUrl("http://127.0.0.1:8080")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecipeTestService::class.java)
    }
}