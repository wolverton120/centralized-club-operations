package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.clubapp.databinding.ActivityNoticeSubmissionBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class notice_submission : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeSubmissionBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeSubmissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        binding.submitNoticeButton.setOnClickListener {
            val title = binding.titleInput.text.toString().trim()
            val content = binding.contentInput.text.toString().trim()
            val submitter = binding.submitterInput.text.toString().trim()

            if (title.isEmpty() || content.isEmpty() || submitter.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val notice = hashMapOf(
                "title" to title,
                "content" to content,
                "submitter" to submitter,
                "timestamp" to FieldValue.serverTimestamp()
            )

            firestore.collection("notices")
                .add(notice)
                .addOnSuccessListener {
                    Toast.makeText(this, "Notice submitted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to submit notice", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

