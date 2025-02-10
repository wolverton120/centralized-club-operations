package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ViewPortalActivity : AppCompatActivity() {

    private lateinit var messagesListView: ListView
    private lateinit var portalHeaderTextView: TextView
    private val db = FirebaseFirestore.getInstance()
    private val messagesList = mutableListOf<Message>()
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_portal)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        messagesListView = findViewById(R.id.messagesListView)
        portalHeaderTextView = findViewById(R.id.textViewPortalHeader)

        startTextAnimation()
        fetchMessages()
    }

    private fun startTextAnimation() {
        val text = "STUDENT PORTAL"
        handler.postDelayed(object : Runnable {
            override fun run() {
                val builder = StringBuilder()
                for (i in text.indices) {
                    handler.postDelayed({
                        builder.append(text[i])
                        portalHeaderTextView.text = builder.toString()
                    }, i * 100L)
                }
                handler.postDelayed(this, 12000)
            }
        }, 0)
    }

    private fun fetchMessages() {
        db.collection("portalmsgs")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { documents ->
                messagesList.clear()
                for (doc in documents) {
                    val user = doc.getString("user") ?: "Anonymous"
                    val message = doc.getString("message") ?: ""
                    val timestamp = doc.getTimestamp("timestamp")?.toDate().toString()
                    messagesList.add(Message(user, message, timestamp))
                }

                val adapter = MessageAdapter(this, messagesList)
                messagesListView.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch messages", Toast.LENGTH_SHORT).show()
            }
    }
}




