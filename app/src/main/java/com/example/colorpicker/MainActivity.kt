package com.example.colorpicker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.lang.String

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var imageView:ImageView
    lateinit var textView: TextView
    lateinit var textView2: TextView
    var scaledBitmap:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView=findViewById(R.id.imageView)
        textView=findViewById(R.id.textView)
        textView2=findViewById(R.id.textView2)
    }

    fun buttonHandler(view:View)
    {
        dispatchTakePictureIntent()
    }



    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, 1)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            val bm: BitmapDrawable = imageView.getDrawable() as BitmapDrawable
            var bitmap=bm.bitmap as Bitmap
            val displayMetrics = DisplayMetrics()
            var width = displayMetrics.widthPixels
            var height = displayMetrics.heightPixels
            scaledBitmap= Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                false
            )
            imageView.getLayoutParams().height =bitmap.getHeight() ;
            imageView.getLayoutParams().width = bitmap.getWidth();




        }
    }





}