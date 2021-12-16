package com.example.gomoku

import android.graphics.Point
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromArrayList(value: ArrayList<Point>): String{
        return Gson().toJson(value)
    }
    @TypeConverter
    fun fromString(value: String) : ArrayList<Point>{
        val listType: Type = object : TypeToken<ArrayList<Point>>() {}.type
        return Gson().fromJson(value, listType)
    }
}