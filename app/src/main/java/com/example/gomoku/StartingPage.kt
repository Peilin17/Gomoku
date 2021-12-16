package com.example.gomoku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController


class StartingPage : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_starting_page, container, false)
        if (MyViewModel.newBackground != null){
            view.findViewById<ConstraintLayout>(R.id.startingLayout).background = MyViewModel.newBackground
        }
        else {
            view.findViewById<ConstraintLayout>(R.id.startingLayout).background =
                resources.getDrawable(MyViewModel.background)
        }
        var pve = view.findViewById<Button>(R.id.start_pve_button)
        pve.setOnClickListener {
            (activity as MainActivity).closeToolBar()
            view.findNavController().navigate(R.id.action_startingPage_to_pvEPage)
        }
        var pvp = view.findViewById<Button>(R.id.start_pvp_button)
        pvp.setOnClickListener {
            (activity as MainActivity).closeToolBar()
            view.findNavController().navigate(R.id.action_startingPage_to_pvPPage)
        }
        var online = view.findViewById<Button>(R.id.start_online_button)
        online.setOnClickListener {
            //view.findNavController().navigate(R.id.action_startingPage_to_Page)
        }
        if (MyViewModel.textBright){
            pve.background = resources.getDrawable(R.drawable.pve_button_image_light)
            pvp.background = resources.getDrawable(R.drawable.pvp_button_image_light)
            online.background = resources.getDrawable(R.drawable.online_button_image_light)
        }



        return view
    }

    override fun onResume() {
        (activity as MainActivity).showToolBar()
        super.onResume()
    }


}