package com.example.gomoku

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import kotlinx.coroutines.*


class MatchHistory : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match_history, container, false)
        if (MyViewModel.newBackground != null) {
            view.findViewById<ConstraintLayout>(R.id.frameLayout).background =
                MyViewModel.newBackground
        } else {
            view.findViewById<ConstraintLayout>(R.id.frameLayout).background =
                resources.getDrawable(MyViewModel.background)
        }
        view.findViewById<TextView>(R.id.match_title).text = MyViewModel.show_title
        val chessboard = view.findViewById<AIChessboardView>(R.id.AIChessboardView)
        chessboard.mwhiteArray = MyViewModel.show_white!!
        chessboard.mblackArray = MyViewModel.show_black!!
        chessboard.isClickable = false

        var delete = view.findViewById<Button>(R.id.match_delete)
        delete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val scope = async {
                    MyViewModel.delete()
                    MyViewModel.getAll()
                    MyViewModel.getCount()
                }
                scope.await()
                view.findNavController().navigate(R.id.action_matchHistory_to_matchHistoryList)

            }
        }

        var load = view.findViewById<Button>(R.id.match_load)
        load.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)

            builder.setTitle("Loading Game") //设置弹出对话框的标题

            //builder.setIcon(R.drawable.ic_launcher) //设置弹出对话框的图标

            builder.setMessage("Which mode you want?") //设置弹出对话框的内容

            builder.setCancelable(true) //能否被取消

            //正面的按钮（肯定）

            builder.setPositiveButton("Computer Player", DialogInterface.OnClickListener { dialog, which ->
                MyViewModel.cur_black = MyViewModel.show_black
                MyViewModel.cur_white = MyViewModel.show_white
                view.findNavController().navigate(R.id.action_matchHistory_to_pvEPage)
                dialog.cancel()


            })
            //反面的按钮（否定）
            builder.setNegativeButton("PvP", DialogInterface.OnClickListener { dialog, which ->

                MyViewModel.pvp_black = MyViewModel.show_black
                MyViewModel.pvp_white = MyViewModel.show_white
                view.findNavController().navigate(R.id.action_matchHistory_to_pvPPage)
                dialog.cancel()
            })

            builder.show()
        }

        if (MyViewModel.textBright){
            load.setTextColor(Color.WHITE)
            delete.setTextColor(Color.WHITE)
            view.findViewById<TextView>(R.id.match_title).setTextColor(Color.WHITE)
        }

        return view
    }

    companion object {

//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MatchHistory().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}