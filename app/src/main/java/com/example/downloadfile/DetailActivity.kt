package com.example.downloadfile

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.downloadfile.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        )?.cancelNotifications()

        val downloadId = intent.extras?.getLong(DOWNLOAD_ID)

        if(downloadId != null) {
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(DownloadManager.COLUMN_URI)
                val uri = cursor.getString(idx)
                binding.detailLayout.textViewDownload.text = uri
            }
        }

        binding.detailLayout.backButton.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

    }
}
