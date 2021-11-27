
package com.ezam.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezam.networkresponseadapter.NetworkResponse

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lazy {  }
    }

    fun test(){
        NetworkResponse.Success("ds")
    }
}