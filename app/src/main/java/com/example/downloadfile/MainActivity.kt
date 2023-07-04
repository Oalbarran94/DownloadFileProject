package com.example.downloadfile

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.downloadfile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var binding: ActivityMainBinding

    private lateinit var pendingIntent: PendingIntent
    private var selectedLink: String? = null
    private var selectedName: String? = null
    private var downloadStatusCode: String = "Loading"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        binding.includedLayout.customButton.setLoadingButtonState(ButtonState.Completed)
        binding.includedLayout.customButton.setOnClickListener {
            binding.includedLayout.customButton.setLoadingButtonState(ButtonState.Loading)
            Handler().postDelayed(this::download, 5000)
        }
    }

    @SuppressLint("Range")
    private fun download() {
        if (selectedLink != null) {
            val request =
                DownloadManager.Request(Uri.parse(selectedLink))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(DownloadManager.Query())
            var status = 0
            if (cursor.moveToFirst()) {
                status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }
            setDownloadStatus(status)
            downloadID =
                downloadManager.enqueue(request)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "channelId",
                    getString(R.string.notificationChannelName),
                    NotificationManager.IMPORTANCE_LOW
                )
                channel.description = "Channel Description"

                val notificationManager = getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(channel)
            }

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("selectedName", selectedName);
            intent.putExtra("downloadStatus", downloadStatusCode)

            pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, "channelId")
                    .setSmallIcon(androidx.core.R.drawable.notification_template_icon_bg)
                    .setContentTitle(getString(R.string.notificationTitle))
                    .setContentText(getString(R.string.notificationDescription))
                    .setContentIntent(pendingIntent)
                    .addAction(
                        0,
                        "Check the status",
                        pendingIntent
                    )
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(this)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            notificationManager.notify(0, builder.build())
            binding.includedLayout.customButton.setLoadingButtonState(ButtonState.Completed)

        } else Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT)
            .show()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val isChecked = view.isChecked
            when (view.getId()) {
                R.id.firstOption ->
                    if (isChecked) {
                        selectedLink = "https://github.com/bumptech/glide"
                        selectedName = getString(R.string.option1)
                    }

                R.id.secondOption ->
                    if (isChecked) {
                        selectedLink =
                            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                        selectedName = getString(R.string.option2)
                    }

                R.id.thirdOption -> {
                    if (isChecked) {
                        selectedLink = "https://github.com/square/retrofit"
                        selectedName = getString(R.string.option3)
                    }
                }
            }
        }
    }

    private fun setDownloadStatus(statusCode: Int) {
        when(statusCode){
            DownloadManager.STATUS_FAILED -> {
                downloadStatusCode = "Failed"
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                downloadStatusCode = "Success"
            }
            else -> {
                downloadStatusCode
            }
        }
    }
}