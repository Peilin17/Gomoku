package com.example.gomoku

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDataDao{
    @Query("SELECT * FROM GameData")
    fun getAll(): List<GameData>

    @Insert
    fun insert(game: GameData)

    @Delete
    fun delete(game: GameData)

    @Query("SELECT COUNT(uid) FROM GameData")
    fun getNumber(): Int

    @Query("SELECT * FROM GameData Where uid == :id")
    fun check(id: Int) : GameData

    @Query("DELETE FROM GameData")
    fun nukeTable()
}