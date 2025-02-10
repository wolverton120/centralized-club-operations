//Unused class after club gallery function removed
package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.clubapp.databinding.ActivityClubgalleryBinding

class clubgallery : AppCompatActivity() {

    private lateinit var binding: ActivityClubgalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClubgalleryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.uploadButton.setOnClickListener {
            val intent = Intent(this, UploadImageActivity::class.java)
            startActivity(intent)
        }

        binding.browseGalleryButton.setOnClickListener {
            val intent = Intent(this, BrowseGalleryActivity::class.java)
            startActivity(intent)
        }
    }
}
