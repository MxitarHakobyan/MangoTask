package com.mango.task.di

import com.google.gson.Gson
import com.mango.task.data.localStorage.prefs.SecureStorage
import com.mango.task.data.localStorage.prefs.SecureStorage.Keys.KEY_ACCESS_TOKEN
import com.mango.task.data.localStorage.prefs.SecureStorage.Keys.KEY_IS_LOGGED_IN
import com.mango.task.data.network.RefreshTokenService
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
    fun provideRefreshTokenService(retrofit: Retrofit): RefreshTokenService =
        retrofit.create(RefreshTokenService::class.java)

    @Provides
    @Singleton
    fun provideUsersService(retrofit: Retrofit): UsersService =
        retrofit.create(UsersService::class.java)


    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenAuthenticator: TokenAuthenticator,
        secureStorage: SecureStorage,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient().newBuilder()
        builder.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            if (secureStorage.getBoolean(KEY_IS_LOGGED_IN)) {
                requestBuilder.addHeader(
                    "Authorization", "Bearer ${
                        secureStorage.get(
                            KEY_ACCESS_TOKEN
                        )
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
        .baseUrl("https://plannerok.ru/api/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}