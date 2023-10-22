package com.bookshelf.app.core.di

import com.bookshelf.app.dashboard.data.datasource.DashboardDataSource
import com.bookshelf.app.registration.data.datasource.RegistrationDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module responsible for providing data sources for different features of the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    /**
     * Provides a data source for registration-related operations.
     *
     * @param retrofit An instance of Retrofit for creating the data source.
     * @return An implementation of the RegistrationDataSource interface.
     */
    @Provides
    @Singleton
    fun provideRegistrationDataSource(retrofit: Retrofit): RegistrationDataSource {
        return retrofit.create(RegistrationDataSource::class.java)
    }

    /**
     * Provides a data source for dashboard-related operations.
     *
     * @param retrofit An instance of Retrofit for creating the data source.
     * @return An implementation of the DashboardDataSource interface.
     */
    @Provides
    @Singleton
    fun provideDashboardDataSource(retrofit: Retrofit): DashboardDataSource {
        return retrofit.create(DashboardDataSource::class.java)
    }
}
