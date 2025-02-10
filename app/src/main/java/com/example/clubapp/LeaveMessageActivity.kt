package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class LeaveMessageActivity : AppCompatActivity() {

    private lateinit var messageField: EditText
    private lateinit var submitMessageButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_message)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        messageField = findViewById(R.id.messageField)
        submitMessageButton = findViewById(R.id.submitMessageButton)

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to submit a message", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val userId = currentUser.uid

        db.collection("students").document(userId).get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name") ?: "Unknown User"

                submitMessageButton.setOnClickListener {
                    val message = messageField.text.toString().trim()

                    if (message.isNotBlank()) {
                        val messageData = mapOf(
                            "user" to userName,
                            "userId" to userId,
                            "message" to message,
                            "timestamp" to FieldValue.serverTimestamp()
                        )

                        db.collection("portalmsgs").add(messageData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Message submitted successfully", Toast.LENGTH_SHORT).show()
                                messageField.text.clear()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to submit message", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
    }
}



