package com.example.pokeapi.di

import android.app.Application
import androidx.room.Room
import com.example.pokeapi.data.local.dao.AbilityDao
import com.example.pokeapi.data.local.dao.ItemDao
import com.example.pokeapi.data.local.dao.PokeDao
import com.example.pokeapi.data.local.database.PokeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): PokeDatabase {
        return Room.databaseBuilder(app, PokeDatabase::class.java,"poke_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePokeDao(db: PokeDatabase): PokeDao {
        return db.pokeDao()
    }
    @Provides
    @Singleton
    fun provideItemDao(db: PokeDatabase): ItemDao {
        return db.itemDao()
    }
    @Provides
    @Singleton
    fun providesAbilityDao(db: PokeDatabase): AbilityDao {
        return db.abilityDao()
    }
}