package com.example.firebasecommunityapp.presentation.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModel {
    @Module
    @InstallIn(SingletonComponent::class)
    object FirebaseModule {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirestore() = FirebaseDatabase.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseStorage() = FirebaseStorage.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseStore(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }
    }
}