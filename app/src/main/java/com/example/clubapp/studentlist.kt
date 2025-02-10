package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class studentlist : AppCompatActivity() {
    private lateinit var headerTextView: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentlist)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.studentListHeader)
        startTextAnimation()

        val db = FirebaseFirestore.getInstance()
        val studentListLayout = findViewById<LinearLayout>(R.id.studentListLayout)

        db.collection("students")
            .get()
            .addOnSuccessListener { documents ->
                var index = 1
                for (document in documents) {
                    val name = document.getString("name") ?: "Unknown"
                    val id = document.getString("id") ?: "ID not available"

                    val studentNameTextView = TextView(this).apply {
                        text = "$index. $name"
                        textSize = 20f
                        setPadding(10, 20, 10, 0)
                        setTextColor(resources.getColor(android.R.color.black, null))
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }

                    val studentIdTextView = TextView(this).apply {
                        text = "ID: $id"
                        textSize = 18f
                        setPadding(10, 0, 10, 20)
                        setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    }

                    studentListLayout.addView(studentNameTextView)
                    studentListLayout.addView(studentIdTextView)

                    index++
                }
            }
            .addOnFailureListener { e ->
                val errorMessage = TextView(this).apply {
                    text = "Error fetching data: ${e.message}"
                    textSize = 18f
                    setPadding(10, 10, 10, 10)
                    setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                }
                studentListLayout.addView(errorMessage)
            }
    }

    private fun startTextAnimation() {
        val text = "STUDENT LIST"
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


