package com.bookshelf.app.core.di

import com.bookshelf.app.registration.data.repository.RegistrationRepoImpl
import com.bookshelf.app.registration.domain.repository.RegistrationRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun provideRegistrationRepo(repo: RegistrationRepoImpl): RegistrationRepo

}