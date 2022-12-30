package com.example.gservices.di

import com.example.gservices.data.auth.AuthRepository
import com.example.gservices.data.auth.AuthRepositoryImpl
import com.example.gservices.data.categories.CategoryRepository
import com.example.gservices.data.categories.CategoryRepositoryImpl
import com.example.gservices.data.user.UserRepository
import com.example.gservices.data.user.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideAuthRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
    ): AuthRepository {
        return AuthRepositoryImpl(auth,database)
    }

    @Provides
    fun provideUserRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
    ): UserRepository {
        return UserRepositoryImpl(auth,database)
    }

    @Provides
    fun provideCategoryRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
    ): CategoryRepository {
        return CategoryRepositoryImpl(auth,database)
    }
}