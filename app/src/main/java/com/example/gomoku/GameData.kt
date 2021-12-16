package com.example.gomoku

import android.graphics.Point
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameData(
    @PrimaryKey val uid: Int,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "Black") val black_list: ArrayList<Point>,
    @ColumnInfo(name = "White") val white_list: ArrayList<Point>
)