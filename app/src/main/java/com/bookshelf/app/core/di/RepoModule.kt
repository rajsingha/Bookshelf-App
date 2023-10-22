package com.bookshelf.app.core.di

import com.bookshelf.app.dashboard.data.repository.DashboardRepoImpl
import com.bookshelf.app.dashboard.domain.repository.DashboardRepo
import com.bookshelf.app.registration.data.repository.RegistrationRepoImpl
import com.bookshelf.app.registration.domain.repository.RegistrationRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module responsible for binding concrete implementations to their corresponding repository interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    /**
     * Binds the [RegistrationRepoImpl] concrete implementation to the [RegistrationRepo] interface.
     *
     * @param repo An instance of [RegistrationRepoImpl] providing registration-related repository functionality.
     * @return An instance of [RegistrationRepo] bound to [RegistrationRepoImpl].
     */
    @Singleton
    @Binds
    abstract fun provideRegistrationRepo(repo: RegistrationRepoImpl): RegistrationRepo

    /**
     * Binds the [DashboardRepoImpl] concrete implementation to the [DashboardRepo] interface.
     *
     * @param repo An instance of [DashboardRepoImpl] providing dashboard-related repository functionality.
     * @return An instance of [DashboardRepo] bound to [DashboardRepoImpl].
     */
    @Singleton
    @Binds
    abstract fun provideDashboardRepo(repo: DashboardRepoImpl): DashboardRepo
}
