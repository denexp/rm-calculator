package com.denisdev.repository.di

import com.denisdev.domain.repository.AuthorRepository
import com.denisdev.repository.AuthorRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    companion object {
        @Provides
        fun authorRepository(): AuthorRepository = AuthorRepositoryImpl()
    }
}
