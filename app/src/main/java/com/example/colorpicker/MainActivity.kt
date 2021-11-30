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
import androidx.lifecycle.Observer
import java.lang.String
import java.util.Collections.max
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var imageView:ImageView
    lateinit var textView: TextView
    lateinit var textView2: TextView
    lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView=findViewById(R.id.imageView)
        textView=findViewById(R.id.textView)
        textView2=findViewById(R.id.textView2)
        apiService= ApiService()

        apiService.getColorNameLive().observe(this, Observer {
            textView2.text=it


        })
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
        textView2.text="Pick a point on the image"
        textView.text=""
        textView.rootView.setBackgroundColor(Color.WHITE)

        textView.setTextColor(Color.BLACK)
        textView2.setTextColor(Color.BLACK)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            val bm: BitmapDrawable = imageView.getDrawable() as BitmapDrawable
            var scaledBitmap=bm.bitmap as Bitmap
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            var width = displayMetrics.widthPixels
            var in1=width/scaledBitmap.getWidth()




            scaledBitmap= Bitmap.createScaledBitmap(
                scaledBitmap,
                width,
                scaledBitmap.getHeight()*in1,
                false
            )
            imageView.setImageBitmap(scaledBitmap)

            imageView.getLayoutParams().height =scaledBitmap.getHeight() ;
            imageView.getLayoutParams().width = scaledBitmap.getWidth();


            imageView.setOnTouchListener(object: View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    val touchX = event!!.x.toInt()
                    val touchY = event.y.toInt()
                    getColor(scaledBitmap,touchX,touchY)

                    return true
                }
            })

        }
    }

    fun getColor(bitmap: Bitmap,x:Int,y:Int) {

        if(y<bitmap.getHeight()  && x<bitmap.getWidth() && x>=0 && y>=0)
        {
            val p: Int = bitmap.getPixel(x, y)
            textView.text="rgb("+ Color.red(p).toString()+","+ Color.green(p).toString()+","+ Color.blue(p).toString()+")"
            textView.rootView.setBackgroundColor(Color.rgb(Color.red(p), Color.green(p), Color.blue(p)))
            val hex = String.format("%02x%02x%02x", Color.red(p), Color.green(p), Color.blue(p))
            val textColor: Int = Color.rgb(
                255 - Color.red(p),
                255 - Color.green(p),
                255 - Color.blue(p)
            )
            textView.setTextColor(textColor)
            textView2.setTextColor(textColor)
            apiService.loadData(hex)

        }



    }



}