package com.example.gomoku

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.balysv.materialripple.MaterialRippleLayout
import kotlinx.coroutines.*
import java.io.File



class ChangeBg : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_bg, container, false)
        val newButton = view.findViewById<Button>(R.id.bg_change_button)
        if (MyViewModel.newBackground != null) {
            view.findViewById<ConstraintLayout>(R.id.change_bg_layout).background =
                MyViewModel.newBackground
            if (MyViewModel.textBright) {
                newButton.background =
                    resources.getDrawable(R.drawable.change_bg_button_light)
            }
        } else {
            view.findViewById<ConstraintLayout>(R.id.change_bg_layout).background =
                resources.getDrawable(R.drawable.default_bg)
        }


        var upload = view.findViewById<Button>(R.id.bg_change_upload)


        var reset = view.findViewById<Button>(R.id.bg_change_reset)
        reset.setOnClickListener {
            view.findViewById<ConstraintLayout>(R.id.change_bg_layout).background =
                resources.getDrawable(R.drawable.default_bg)
            reset.setTextColor(Color.BLACK)
            upload.setTextColor(Color.BLACK)
            newButton.background =
                resources.getDrawable(R.drawable.change_bg_button_black)
            MyViewModel.textBright = false
            MyViewModel.newBackground = null
        }

        newButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                try {
                    val task1 = async { MyViewModel.getRandomPhoto() }
                    view.findViewById<ProgressBar>(R.id.bg_change_animation).visibility =
                        View.VISIBLE
                    task1.await()
                    view.findViewById<ConstraintLayout>(R.id.change_bg_layout).background =
                        MyViewModel.newBackground
                    if (!isPixelColorBright(MyViewModel.color!!)) {
                        MyViewModel.textBright = true
                        newButton.background =
                            resources.getDrawable(R.drawable.change_bg_button_light)
                        reset.setTextColor(Color.WHITE)
                        upload.setTextColor(Color.WHITE)

                    } else {
                        MyViewModel.textBright = false
                        newButton.background =
                            resources.getDrawable(R.drawable.change_bg_button_black)
                        reset.setTextColor(Color.BLACK)
                        upload.setTextColor(Color.BLACK)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Unknown Error, pls try again", Toast.LENGTH_SHORT)
                        .show()
                }


                view.findViewById<ProgressBar>(R.id.bg_change_animation).visibility = View.GONE

            }
        }
        upload.setOnClickListener {
            if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery();
            }


        }


        val airplaneObserver = Observer<Boolean>{ mode ->
            if (mode){

                newButton.isEnabled = false
//                view.findViewById<MaterialRippleLayout>(R.id.bg_ripple1).isClickable = false
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
            }
            else{
                newButton.isEnabled = true
                //view.findViewById<MaterialRippleLayout>(R.id.bg_ripple1).isClickable = true
                //newButton.isClickable = true
            }
        }
        MyViewModel.airplane.observe(this, airplaneObserver)

        if (MyViewModel.textBright){
            upload.setTextColor(Color.WHITE)
            reset.setTextColor(Color.WHITE)

        }

        return view
    }

    private fun isPixelColorBright(color: Int): Boolean {
        if (android.R.color.transparent == color) return true
        var rtnValue = false
        val rgb = intArrayOf(Color.red(color), Color.green(color), Color.blue(color))
        val brightness = Math.sqrt(
            rgb[0] * rgb[0] * .299 + (rgb[1]
                    * rgb[1] * .587) + rgb[2] * rgb[2] * .114
        ).toInt()
        if (brightness >= 125) {    // light color
            rtnValue = true
        }
        return rtnValue
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val file = File(getRealPathFromURI(data!!.data!!)!!)

            if (file.exists()) {
                val d = Drawable.createFromPath(file.absolutePath)
                MyViewModel.newBackground = d
                view!!.findViewById<ConstraintLayout>(R.id.change_bg_layout).background =
                    MyViewModel.newBackground

            }
        }
    }

    ///storage/emulated/0/Download/iPhone-XS-marketing-wallpaper.jpg
    private fun getRealPathFromURI(contentURI: Uri): String? {
        val cursor: Cursor? = context!!.contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) { // Source is Dropbox or other similar local file path
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1001 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    pickImageFromGallery()
                } else {

                }
                return
            }


            else -> {
                // Ignore all other requests.
            }
        }
    }

    companion object {
        //image pick code
        const val IMAGE_PICK_CODE = 1000;

        //Permission code
        const val PERMISSION_CODE = 1001;
    }


}