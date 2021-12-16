package com.example.gomoku

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.coroutines.*


class PvEPage : Fragment() {

    lateinit var chessboard: AIChessboardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pv_e_page, container, false)
        if (MyViewModel.newBackground != null) {
            view.findViewById<ConstraintLayout>(R.id.pveLayout).background =
                MyViewModel.newBackground
        } else {
            view.findViewById<ConstraintLayout>(R.id.pveLayout).background =
                resources.getDrawable(MyViewModel.background)
        }

        chessboard = view.findViewById<AIChessboardView>(R.id.boardView_AI)
        if (MyViewModel.cur_white != null || MyViewModel.cur_black != null) {
            chessboard.mblackArray = MyViewModel.cur_black!!
            chessboard.mwhiteArray = MyViewModel.cur_white!!
            chessboard.invalidate()
        }
        var regretButton = view.findViewById<Button>(R.id.pve_regret_button)
        regretButton.setOnClickListener {
            chessboard.regretPlay()
        }
        var restartButton = view.findViewById<Button>(R.id.pve_restart_button)
        restartButton.setOnClickListener {
            chessboard.playAgain()
        }
        var savedButton = view.findViewById<Button>(R.id.pve_saved_button)
        savedButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_pvEPage_to_matchHistoryList)
        }
        var saveButton = view.findViewById<Button>(R.id.pve_save_button)
        saveButton.setOnClickListener {
            val inflater = this.layoutInflater
            val v = inflater.inflate(R.layout.dialog_plaintext, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)

            builder.setTitle("Saving game...") //设置弹出对话框的标题

            //builder.setIcon(R.drawable.ic_launcher) //设置弹出对话框的图标

            builder.setMessage("Give a name") //设置弹出对话框的内容
            builder.setView(v)
            builder.setCancelable(false) //能否被取消

            //正面的按钮（肯定）

            builder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->


                val t = v.findViewById<EditText>(R.id.dialog_editText).text.toString()
                val scope = CoroutineScope(Dispatchers.Main).async {
                    withContext(Dispatchers.IO) {
                        MyViewModel.db!!.GameDataDao().insert(
                            GameData(
                                MyViewModel.dataCount,
                                t,
                                chessboard.mblackArray,
                                chessboard.mwhiteArray
                            )
                        )
                        MyViewModel.getCount()
                        MyViewModel.getAll()
                    }
//                    view.findViewById<ProgressBar>(R.id.pve_saving_animation).visibility = View.VISIBLE


                }
                // waiting animation
                // seems it runs too fast so we can not see that.
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Main) {
                        view.findViewById<ProgressBar>(R.id.pve_saving_animation).visibility =
                            View.VISIBLE
                        scope.await()
                        view.findViewById<ProgressBar>(R.id.pve_saving_animation).visibility =
                            View.GONE
                        Toast.makeText(context, "Game Saved!", Toast.LENGTH_SHORT).show()
                    }

                }



                dialog.cancel()


            })
            //反面的按钮（否定）
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })

            builder.show()

        }

        if (MyViewModel.textBright){
            saveButton.setTextColor(Color.WHITE)
            savedButton.setTextColor(Color.WHITE)
            regretButton.setTextColor(Color.WHITE)
            restartButton.setTextColor(Color.WHITE)

        }



        return view
    }
//    override fun onBackPressed()
//    {
//        view!!.findNavController().navigate(R.id.action_pvEPage_to_startingPage)
//    }
    override fun onPause() {
        MyViewModel.cur_white = chessboard.mwhiteArray
        MyViewModel.cur_black = chessboard.mblackArray
        super.onPause()

    }


}