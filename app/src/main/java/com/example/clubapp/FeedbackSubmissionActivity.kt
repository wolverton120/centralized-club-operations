package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackSubmissionActivity : AppCompatActivity() {
    private lateinit var submitterField: EditText
    private lateinit var feedbackField: EditText
    private lateinit var submitFeedbackBtn: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_submission)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        submitterField = findViewById(R.id.submitterField)
        feedbackField = findViewById(R.id.feedbackField)
        submitFeedbackBtn = findViewById(R.id.submitFeedbackBtn)

        submitFeedbackBtn.setOnClickListener {
            val submitter = submitterField.text.toString()
            val feedback = feedbackField.text.toString()

            if (submitter.isNotBlank() && feedback.isNotBlank()) {
                val feedbackData = mapOf(
                    "submitter" to submitter,
                    "feedback" to feedback,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                db.collection("feedbacks").add(feedbackData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
                        submitterField.text.clear()
                        feedbackField.text.clear()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

