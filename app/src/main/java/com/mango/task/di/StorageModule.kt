package com.mango.task.di

import android.content.Context
import com.mango.task.data.localStorage.prefs.SecureStorage
import com.mango.task.data.localStorage.prefs.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideSecureStorage(@ApplicationContext context: Context): SecureStorage {
        return SecureStorage(context)
    }

    @Provides
    @Singleton
    fun providePrefsStorage(@ApplicationContext context: Context): SharedPrefs {
        return SharedPrefs(context)
    }
}