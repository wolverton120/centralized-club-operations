package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.clubapp.databinding.ActivityStudentPortalBinding

class StudentPortalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentPortalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentPortalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        binding.leaveMessageButton.setOnClickListener {
            val intent = Intent(this, LeaveMessageActivity::class.java)
            startActivity(intent)
        }

        binding.viewPortalButton.setOnClickListener {
            val intent = Intent(this, ViewPortalActivity::class.java)
            startActivity(intent)
        }
    }
}
