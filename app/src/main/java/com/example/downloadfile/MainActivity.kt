package com.example.downloadfile

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.downloadfile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private var downloadID: Long = 0

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val OPTION1 =
            "https://github.com/bumptech/glide"
        private const val OPTION2 =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        private const val OPTION3 =
            "https://github.com/square/retrofit"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.createChannel(
            "downloadChannelId",
            getString(R.string.channelName)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        binding.includedLayout.customButton.setOnClickListener {

            when (binding.includedLayout.downloadRadioGroup.checkedRadioButtonId) {
                R.id.download1RadioBtn -> {
                    download(OPTION1)
                }
                R.id.download2RadioBtn -> {
                    download(OPTION2)
                }
                R.id.download3RadioBtn -> {
                    download(OPTION3)
                }
                else -> {
                    Toast.makeText(
                        applicationContext,
                        R.string.makeSelection,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun download(url: String) {
        notificationManager.cancelNotifications()
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
        binding.includedLayout.customButton.downloadStarted()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                var message = "Download Completed"
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(DownloadManager.COLUMN_URI)
                    val uri = cursor.getString(idx)
                    message = getString(R.string.notificationDescription, uri)
                }
                Toast.makeText(applicationContext, R.string.downloadCompleted, Toast.LENGTH_SHORT)
                    .show()

                binding.includedLayout.customButton.downloadFinished()
                notificationManager.sendNotification(message, id, "downloadChannelId", applicationContext)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}