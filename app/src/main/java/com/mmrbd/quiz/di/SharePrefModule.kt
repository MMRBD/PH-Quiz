package com.mmrbd.quiz.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.mmrbd.quiz.utils.SharePrefUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SharePrefModule {

    @Provides
    @Singleton
    fun provideSharePref(@ApplicationContext application: Context): SharedPreferences {
        return application.getSharedPreferences("quiz_share_pref", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSharePrefUtils(sharedPreferences: SharedPreferences): SharePrefUtil {
        return SharePrefUtil(sharedPreferences)
    }

}