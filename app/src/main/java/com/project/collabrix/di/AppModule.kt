package com.project.collabrix.di

import android.content.Context
import com.project.collabrix.data.api.AuthApiService
import com.project.collabrix.data.local.UserPreferences
import com.project.collabrix.data.repository.AuthRepositoryImpl
import com.project.collabrix.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        userPreferences: UserPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(authApiService, userPreferences)
    }
} 