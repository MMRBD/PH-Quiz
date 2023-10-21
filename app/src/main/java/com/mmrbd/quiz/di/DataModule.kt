package com.mmrbd.quiz.di

import com.mmrbd.quiz.data.api.ApiService
import com.mmrbd.quiz.data.repository.Repository
import com.mmrbd.quiz.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideRepository(apiService: ApiService): Repository = RepositoryImpl(apiService)

}