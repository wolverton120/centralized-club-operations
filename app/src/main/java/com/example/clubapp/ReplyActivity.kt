package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReplyActivity : AppCompatActivity() {

    private lateinit var replyEditText: EditText
    private lateinit var submitReplyButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var messageText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        replyEditText = findViewById(R.id.replyEditText)
        submitReplyButton = findViewById(R.id.submitReplyButton)

        messageText = intent.getStringExtra("messageText")

        submitReplyButton.setOnClickListener {
            val replyText = replyEditText.text.toString().trim()
            if (replyText.isNotEmpty()) {
                postReply(replyText)
            } else {
                Toast.makeText(this, "Please enter a reply", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postReply(replyText: String) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to reply", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        db.collection("students").document(userId).get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name") ?: "Anonymous"

                val reply = hashMapOf(
                    "user" to userName,
                    "reply" to replyText,
                    "timestamp" to Timestamp.now(),
                    "message" to messageText
                )

                db.collection("portalreplies")
                    .add(reply)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reply posted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to post reply", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
            }
    }
}







