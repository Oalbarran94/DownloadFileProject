package com.example.downloadfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.downloadfile.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        val intent = intent

        val selectedFile = intent.getStringExtra("selectedName")
        val statusCode = intent.getStringExtra("downloadStatus")
        intent.getStringExtra("selectedName")?.let { Log.i("Intent Value", it) }
        intent.getStringExtra("downloadStatus")?.let {  Log.i("Intent Value status", it)}

        binding.detailLayout.selectedFileOption.text = selectedFile
        binding.detailLayout.status.text = statusCode

        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        binding.detailLayout.OK.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}
