package com.example.gomoku

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MyViewModel: ViewModel() {
    companion object{
        var screenWidth = 0
        var background = R.drawable.default_bg
        var newBackground: Drawable? = null
        var color: Int? = null
        var textBright :Boolean = false
        var db: AppDatabase?= null
        var dataCount =  0
        var gameDataList :ArrayList<GameData>? = null
        var cur_white: ArrayList<Point>? = null
        var cur_black: ArrayList<Point>? = null
        var pvp_white: ArrayList<Point>? = null
        var pvp_black: ArrayList<Point>? = null
        var show_black: ArrayList<Point>? = null
        var show_white: ArrayList<Point>? = null
        var show_title = ""
        var show_game: GameData? = null
        val airplane: MutableLiveData<Boolean> by lazy {
            MutableLiveData<Boolean>()
        }


        suspend fun getRandomPhoto()
        {
            withContext(Dispatchers.IO){
                val call = RetrofitService.create().getRandomPhoto()
                val response = call.execute().body()
                val raw1 = response?.substringAfter("small\":\"")
                val link = raw1?.substringBefore("\",\"thumb\"")!!
                val raw2 = response?.substringAfter("\"color\":\"")
                val colorString = raw2?.substringBefore("\",\"blur_hash")
                color = Color.parseColor(colorString)
                val x: Bitmap

                val connection: HttpURLConnection = URL(link).openConnection() as HttpURLConnection
                connection.connect()
                val input: InputStream = connection.getInputStream()

                x = BitmapFactory.decodeStream(input)
                newBackground = BitmapDrawable(Resources.getSystem(), x)

            }
        }
        suspend fun getGameData(position: Int){
            withContext(Dispatchers.IO){

                show_game = db!!.GameDataDao().check(position)
                show_black = show_game!!.black_list
                show_white = show_game!!.white_list
                show_title = show_game!!.name
            }
        }
        suspend fun getCount(){
            withContext(Dispatchers.IO){
                dataCount = db!!.GameDataDao().getNumber()
            }

        }
        suspend fun getAll(){
            withContext(Dispatchers.IO){
                gameDataList = ArrayList(db!!.GameDataDao().getAll())
            }
        }

        suspend fun delete(){
            withContext(Dispatchers.IO){
                db!!.GameDataDao().delete(show_game!!)
            }
        }
        suspend fun clear() {
            withContext(Dispatchers.IO){
                db!!.GameDataDao().nukeTable()
            }
        }

    }


//    fun setWidth(w: Int)
//    {
//        screenWidth = w
//    }

}