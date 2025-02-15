package com.example.hegemonycompose.data.di

import android.content.Context
import androidx.room.Room
import com.example.hegemonycompose.data.PlayerDao
import com.example.hegemonycompose.data.PlayerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun providePlayerDao(playerDatabase: PlayerDatabase): PlayerDao {
        return playerDatabase.playerDao()
    }

    @Provides
    @Singleton
    fun providePlayerDatabase(@ApplicationContext appContext: Context): PlayerDatabase {
        return Room.databaseBuilder(appContext, PlayerDatabase::class.java, "PlayerDatabase")
            .build()
    }
}