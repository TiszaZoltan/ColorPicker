package com.example.colorpicker







import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.Fuel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

//Pass Views to handle
class ApiService: ViewModel() {

    private var loadingInProgress:Boolean=false
    private val colorName = MutableLiveData<String>()

    fun getColorNameLive(): LiveData<String>{
        return colorName
    }





    fun loadData(colorHex:String) {

        viewModelScope.launch {
            if(!loadingInProgress)
                getColor(colorHex)

        }
    }

    // Async method
    private suspend fun getColor(colorHex:String) {
        return withContext(Dispatchers.IO){
            loadingInProgress=true;


            Fuel.get("https://www.thecolorapi.com/id?hex="+colorHex)
                .response { request, response, result ->
                    val (bytes, error) = result

                    if (bytes != null) {
                        repeat(500)
                        {
                            println(it.toString())
                        }
                        var jsonObject = JSONTokener(String(bytes)).nextValue() as JSONObject
                        jsonObject= JSONTokener(jsonObject.get("name").toString()).nextValue() as JSONObject

                        if(jsonObject.has("value")){
                            var color=jsonObject.get("value").toString()
                            colorName.postValue(color)
                            loadingInProgress=false;
                        }



                    }





                }
        }
    }






}