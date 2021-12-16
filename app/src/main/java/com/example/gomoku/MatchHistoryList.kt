package com.example.gomoku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balysv.materialripple.MaterialRippleLayout
import kotlinx.coroutines.*


class MatchHistoryList : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_match_history_list, container, false)
        if (MyViewModel.newBackground != null) {
            view.findViewById<ConstraintLayout>(R.id.match_history_list_layout).background =
                MyViewModel.newBackground
        } else {
            view.findViewById<ConstraintLayout>(R.id.match_history_list_layout).background =
                resources.getDrawable(MyViewModel.background)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = GameListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }
    inner class GameListAdapter() : RecyclerView.Adapter<GameListAdapter.GameViewHolder>(){

        var gameList = MyViewModel.gameDataList
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): GameListAdapter.GameViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview, parent, false)
            return GameViewHolder(v)
        }

        override fun onBindViewHolder(holder: GameListAdapter.GameViewHolder, position: Int) {
            holder.view.findViewById<TextView>(R.id.card_name).text = gameList!![position].name
            holder.itemView.findViewById<MaterialRippleLayout>(R.id.ripple).setOnClickListener{
                CoroutineScope(Dispatchers.Main).launch {
                    val scope = async {   MyViewModel.getGameData(position)}
                    scope.await()
                    view!!.findNavController().navigate(R.id.action_matchHistoryList_to_matchHistory)

                }


            }
        }

        override fun getItemCount(): Int {
            return gameList!!.size
        }

        inner class GameViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        }
    }



}