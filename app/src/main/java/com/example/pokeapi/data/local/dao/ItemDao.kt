package com.example.pokeapi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokeapi.data.local.entities.ItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): Flow<List<ItemData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(allItemData: List<ItemData>)

    @Delete
    suspend fun deleteAllItemData(allItemData: List<ItemData>)
}