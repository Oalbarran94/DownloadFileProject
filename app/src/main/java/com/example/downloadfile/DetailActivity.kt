package com.example.downloadfile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.downloadfile.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        binding.detailLayout.OK.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}
