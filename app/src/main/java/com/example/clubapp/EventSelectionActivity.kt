package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class EventSelectionActivity : AppCompatActivity() {
    private lateinit var textViewTitle: TextView
    private val fullText = "Choose an Event Option"
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_selection)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        textViewTitle = findViewById(R.id.textViewTitle)

        startTextAnimation()

        val eventListButton = findViewById<Button>(R.id.eventListButton)
        val attendEventButton = findViewById<Button>(R.id.attendEventButton)
        val removeAttendanceButton = findViewById<Button>(R.id.removeAttendanceButton)

        eventListButton.setOnClickListener {
            startActivity(Intent(this, StudentUpcomingEventsActivity::class.java))
        }

        attendEventButton.setOnClickListener {
            startActivity(Intent(this, StudentAttendEventActivity::class.java))
        }

        removeAttendanceButton.setOnClickListener {
            startActivity(Intent(this, RemoveAttendanceActivity::class.java))
        }
    }

    private fun startTextAnimation() {
        val delay = 100L
        val resetInterval = 12000L

        handler.post(object : Runnable {
            override fun run() {
                textViewTitle.text = ""
                currentIndex = 0

                handler.postDelayed(object : Runnable {
                    override fun run() {
                        if (currentIndex < fullText.length) {
                            textViewTitle.text = fullText.substring(0, currentIndex + 1)
                            currentIndex++
                            handler.postDelayed(this, delay)
                        }
                    }
                }, delay)

                handler.postDelayed(this, resetInterval)
            }
        })
    }
}

