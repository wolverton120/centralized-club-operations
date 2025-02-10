package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class FeedbackListActivity : AppCompatActivity() {
    private lateinit var feedbackListLayout: LinearLayout
    private lateinit var headerTextView: TextView
    private val db = FirebaseFirestore.getInstance()
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_list)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.textViewHeader)
        feedbackListLayout = findViewById(R.id.feedbackListLayout)
        startTextAnimation()

        db.collection("feedbacks").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val submitter = document.getString("submitter") ?: "Anonymous"
                    val feedback = document.getString("feedback") ?: "No feedback provided"
                    val timestamp = document.getTimestamp("timestamp")?.toDate()

                    val formattedTimestamp = if (timestamp != null) {
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(timestamp)
                    } else {
                        "No timestamp provided"
                    }

                    val feedbackView = TextView(this).apply {
                        text = "Submitter: $submitter\nFeedback: $feedback\nTimestamp: $formattedTimestamp\n"
                        textSize = 16f
                        setPadding(16, 16, 16, 16)
                        setTextColor(resources.getColor(android.R.color.black, null))
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }
                    feedbackListLayout.addView(feedbackView)
                }
            }
            .addOnFailureListener {
                val errorView = TextView(this).apply {
                    text = "Failed to load feedback."
                    textSize = 16f
                    setPadding(16, 16, 16, 16)
                }
                feedbackListLayout.addView(errorView)
            }
    }

    private fun startTextAnimation() {
        val text = "Student Feedback"
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
}
