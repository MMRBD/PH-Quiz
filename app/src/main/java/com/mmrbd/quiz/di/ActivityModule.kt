package com.mmrbd.quiz.di

import android.content.Context
import com.mmrbd.quiz.utils.network.NetworkFailureMessage
import com.mmrbd.quiz.utils.network.NetworkFailureMessageImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun getNetworkFailureMessage(@ApplicationContext context: Context): NetworkFailureMessage =
        NetworkFailureMessageImpl(context)
}