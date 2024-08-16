package com.app.carousell.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.carousell.R

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}