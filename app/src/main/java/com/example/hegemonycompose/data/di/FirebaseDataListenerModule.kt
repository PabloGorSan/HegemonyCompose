package com.example.hegemonycompose.data.di

import com.example.hegemonycompose.data.FirebaseDataListener
import com.example.hegemonycompose.data.LocalDatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseDataListenerModule {

    @Binds
    abstract fun bindFirebaseDataListener(localDatabaseRepository: LocalDatabaseRepository): FirebaseDataListener
}