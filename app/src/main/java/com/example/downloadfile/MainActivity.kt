package com.example.downloadfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object {
        private const val OPTION1 =
            "https://github.com/bumptech/glide"
        private const val OPTION2 =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        private const val OPTION3 =
            "https://github.com/square/retrofit"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}