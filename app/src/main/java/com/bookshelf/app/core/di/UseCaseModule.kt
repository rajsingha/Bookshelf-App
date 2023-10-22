package com.bookshelf.app.core.di

import android.content.Context
import com.bookshelf.app.dashboard.data.tables.dao.BooksDao
import com.bookshelf.app.dashboard.domain.repository.DashboardRepo
import com.bookshelf.app.dashboard.domain.usecase.DashboardUseCase
import com.bookshelf.app.registration.data.tables.dao.CountryDao
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao
import com.bookshelf.app.registration.domain.repository.RegistrationRepo
import com.bookshelf.app.registration.domain.repository.SessionRepository
import com.bookshelf.app.registration.domain.usecase.RegistrationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Dagger Hilt module responsible for providing instances of use cases for ViewModels.
 */
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    /**
     * Provides a [RegistrationUseCase] instance with its dependencies injected.
     *
     * @param registrationRepo An implementation of the [RegistrationRepo] interface.
     * @param userCredsDao An instance of [UserCredsDao] for user credentials data access.
     * @param countryDao An instance of [CountryDao] for country data access.
     * @param sessionRepo An instance of [SessionRepository] for user session management.
     * @param context The Android application context.
     * @return An instance of [RegistrationUseCase] with its dependencies injected.
     */
    @Provides
    @ViewModelScoped
    fun provideRegistrationUseCase(
        registrationRepo: RegistrationRepo,
        userCredsDao: UserCredsDao,
        countryDao: CountryDao,
        sessionRepo: SessionRepository,
        @ApplicationContext context: Context
    ) = RegistrationUseCase(registrationRepo, userCredsDao, countryDao, sessionRepo, context)

    /**
     * Provides a [DashboardUseCase] instance with its dependencies injected.
     *
     * @param dashboardRepo An implementation of the [DashboardRepo] interface.
     * @param booksDao An instance of [BooksDao] for books data access.
     * @param sessionRepo An instance of [SessionRepository] for user session management.
     * @param context The Android application context.
     * @return An instance of [DashboardUseCase] with its dependencies injected.
     */
    @Provides
    @ViewModelScoped
    fun provideDashboardUseCase(
        dashboardRepo: DashboardRepo,
        booksDao: BooksDao,
        sessionRepo: SessionRepository,
        @ApplicationContext context: Context
    ) = DashboardUseCase(dashboardRepo, sessionRepo, context, booksDao)
}
