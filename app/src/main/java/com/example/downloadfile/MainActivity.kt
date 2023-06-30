package com.example.downloadfile

import android.app.DownloadManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.downloadfile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        binding.includedLayout.customButton.downloadStarted()
    }
}