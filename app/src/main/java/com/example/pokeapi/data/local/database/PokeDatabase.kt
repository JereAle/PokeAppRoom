package com.example.pokeapi.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokeapi.data.local.dao.AbilityDao
import com.example.pokeapi.data.local.dao.ItemDao
import com.example.pokeapi.data.local.dao.PokeDao
import com.example.pokeapi.data.local.entities.AbilityData
import com.example.pokeapi.data.local.entities.ItemData
import com.example.pokeapi.data.local.entities.PokeData

@Database(entities = [PokeData::class, ItemData::class, AbilityData::class], version = 4)
abstract class PokeDatabase: RoomDatabase() {
    abstract fun pokeDao(): PokeDao
    abstract fun itemDao(): ItemDao
    abstract fun abilityDao(): AbilityDao
}