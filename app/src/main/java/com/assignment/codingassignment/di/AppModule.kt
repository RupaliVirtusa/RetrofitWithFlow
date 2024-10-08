package com.assignment.codingassignment.di

import android.content.Context
import com.assignment.codingassignment.AssignmentApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext appContext: Context): AssignmentApplication {
        return appContext as AssignmentApplication
    }
}