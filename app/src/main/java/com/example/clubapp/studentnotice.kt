package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.clubapp.databinding.ActivityStudentnoticeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale

class studentnotice : AppCompatActivity() {
    private lateinit var binding: ActivityStudentnoticeBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var headerTextView: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentnoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.textView23)
        startTextAnimation()

        loadNotices()
    }

    private fun startTextAnimation() {
        val text = "NOTICE BOARD"
        handler.postDelayed(object : Runnable {
            override fun run() {
                val builder = StringBuilder()
                for (i in text.indices) {
                    handler.postDelayed({
                        builder.append(text[i])
                        headerTextView.text = builder.toString()
                    }, i * 100L)
                }
                handler.postDelayed(this, 12000)
            }
        }, 0)
    }

    private fun loadNotices() {
        val noticeContainer = binding.root.findViewById<LinearLayout>(R.id.noticeContainer)
        firestore.collection("notices")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val title = document.getString("title") ?: "No Title"
                    val content = document.getString("content") ?: "No Content"
                    val submitter = document.getString("submitter") ?: "Unknown"
                    val timestamp = document.getTimestamp("timestamp")?.toDate()

                    val formattedTimestamp = if (timestamp != null) {
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(timestamp)
                    } else {
                        "No timestamp provided"
                    }

                    val noticeView = TextView(this).apply {
                        text = "Title: $title\nContent: $content\nSubmitted by: $submitter\nTimestamp: $formattedTimestamp\n"
                        textSize = 16f
                        setPadding(16, 16, 16, 16)
                    }

                    noticeContainer.addView(noticeView)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load notices", Toast.LENGTH_SHORT).show()
            }
    }
}



