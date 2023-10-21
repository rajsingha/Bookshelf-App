package com.bookshelf.app.core.di

import android.content.Context
import com.bookshelf.app.core.annotations.BaseUrl
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CACHE_SIZE: Long = (10 * 1024 * 1024).toLong()
    private const val TIME_OUT: Long = 100L

    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideBaseUrl(): String {
        // Return the default base URL
        return "https://example.com/api/"
    }

    @Provides
    fun provideCustomInterceptor(@BaseUrl baseUrl: String): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url
                .newBuilder()
                .scheme("https") // Change the scheme if needed (e.g., http)
                .host(baseUrl)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }

    @Provides
    fun provideRetrofit(@BaseUrl baseUrl: String, interceptor: Interceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}