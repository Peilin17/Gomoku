package com.example.gomoku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val MAX_COUNT_IN_LINE = 5
const val MAX_LINE = 15
lateinit var toolbar: Toolbar
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<Toolbar>(R.id.startingToolbar)
        setSupportActionBar(toolbar)
        val appBar = supportActionBar!!
        appBar!!.title = ""
        appBar.setDisplayUseLogoEnabled(true)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        //var height = displayMetrics.heightPixels
        MyViewModel.screenWidth = width
        val db1 = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Game Database"
        ).build()
        MyViewModel.db = db1
        CoroutineScope(Dispatchers.Main).launch{
            MyViewModel.getCount()
            MyViewModel.getAll()
        }
        MyViewModel.airplane.value = false
        val intentFilter = IntentFilter("android.intent.action.AIRPLANE_MODE")
        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    MyViewModel.airplane.value = intent.getBooleanExtra("state", false)
                }
            }
        }

        registerReceiver(receiver, intentFilter)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // inflate the menu into toolbar
        val inflater = menuInflater
        inflater.inflate(R.menu.starting_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }
    fun closeToolBar(){
        supportActionBar!!.hide()
    }
    fun showToolBar(){
        supportActionBar!!.show()
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        /* handle selected (pressed) toolbar menu items */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        when (item?.itemId) {
            R.id.action_0 -> {
                supportActionBar!!.hide()
                navController.navigate(R.id.action_startingPage_to_changeBg)
            }
            R.id.action_1 -> {
                supportActionBar!!.hide()
                navController.navigate(R.id.action_startingPage_to_matchHistoryList)
            }

            R.id.action_3 -> {
                //clean data
                CoroutineScope(Dispatchers.Main).launch {
                    MyViewModel.clear()
                    MyViewModel.getAll()
                    MyViewModel.getCount()
                }

            }

        }
        return super.onOptionsItemSelected(item)
    }


}