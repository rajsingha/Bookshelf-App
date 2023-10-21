package com.bookshelf.app.core.di

import android.content.Context
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao
import com.bookshelf.app.registration.domain.repository.SessionRepository
import com.bookshelf.app.registration.domain.usecase.RegistrationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideRegistrationUseCase(
        userCredsDao: UserCredsDao,
        sessionRepo: SessionRepository,
        @ApplicationContext context: Context
    ) = RegistrationUseCase(userCredsDao,sessionRepo,context)

}