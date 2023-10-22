package com.bookshelf.app.core.di

import com.bookshelf.app.dashboard.data.datasource.DashboardDataSource
import com.bookshelf.app.registration.data.datasource.RegistrationDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRegistrationDataSource(retrofit: Retrofit): RegistrationDataSource {
        return retrofit.create(RegistrationDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardDataSource(retrofit: Retrofit): DashboardDataSource {
        return retrofit.create(DashboardDataSource::class.java)
    }
}