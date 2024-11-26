package com.mango.task.di

import com.google.gson.Gson
import com.mango.task.BuildConfig.BASE_URL
import com.mango.task.data.localStorage.prefs.SecureStorage
import com.mango.task.data.localStorage.prefs.SharedPrefs
import com.mango.task.data.network.TokenAuthenticator
import com.mango.task.data.network.UsersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUsersService(retrofit: Retrofit): UsersService =
        retrofit.create(UsersService::class.java)


    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenAuthenticator: TokenAuthenticator,
        secureStorage: SecureStorage,
        sharedPrefs: SharedPrefs,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient().newBuilder()
        builder.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            if (sharedPrefs.isLoggedIn()) {
                requestBuilder.addHeader(
                    "Authorization", "Bearer ${
                        secureStorage.getAccessToken()
                    }"
                )
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }.authenticator(tokenAuthenticator)
            .addInterceptor(logging)


        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}