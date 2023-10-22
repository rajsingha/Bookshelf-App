package com.bookshelf.app.core.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Dagger Hilt module responsible for providing network-related components such as OkHttpClient, Retrofit,
 * and logging interceptors for making network requests.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // Constants for cache size and timeouts
    private const val CACHE_SIZE: Long = (10 * 1024 * 1024).toLong()
    private const val TIME_OUT: Long = 100L

    /**
     * Provides an instance of HttpLoggingInterceptor for logging network requests and responses.
     *
     * @return An HttpLoggingInterceptor instance configured to log request and response bodies.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * Provides an instance of Retrofit for making network requests.
     *
     * @param client An instance of OkHttpClient for configuring the network client.
     * @return A Retrofit instance with the base URL, client, and JSON converter factory configured.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://base.url.com") // Specify the base URL for network requests.
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())) // Use Gson for JSON serialization/deserialization.
            .build()
    }

    /**
     * Provides an instance of OkHttp Cache for caching network responses.
     *
     * @param context The application context for accessing the cache directory.
     * @return A Cache instance with the specified cache directory and size.
     */
    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, CACHE_SIZE)
    }

    /**
     * Provides an instance of OkHttpClient for configuring and managing network requests.
     *
     * @param loggingInterceptor An instance of HttpLoggingInterceptor for request/response logging.
     * @param cache An instance of OkHttp Cache for response caching.
     * @return An OkHttpClient instance with the specified timeouts and interceptors.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        cache: Cache
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        clientBuilder.addInterceptor(loggingInterceptor)
        clientBuilder.cache(cache)
        return clientBuilder.build()
    }
}
