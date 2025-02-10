package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class StudentUpcomingEventsActivity : AppCompatActivity() {

    private lateinit var eventsListView: ListView
    private lateinit var eventList: MutableList<String>
    private lateinit var textViewHeader: TextView
    private val fullText = "Upcoming Events"
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_upcoming_events)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        textViewHeader = findViewById(R.id.textViewHeader)
        eventsListView = findViewById(R.id.eventsListView)
        eventList = mutableListOf()

        startTextAnimation()
        fetchUpcomingEvents()
    }

    private fun startTextAnimation() {
        val delay = 100L
        val resetInterval = 12000L

        handler.post(object : Runnable {
            override fun run() {
                textViewHeader.text = ""
                currentIndex = 0

                handler.postDelayed(object : Runnable {
                    override fun run() {
                        if (currentIndex < fullText.length) {
                            textViewHeader.text = fullText.substring(0, currentIndex + 1)
                            currentIndex++
                            handler.postDelayed(this, delay)
                        }
                    }
                }, delay)

                handler.postDelayed(this, resetInterval)
            }
        })
    }

    private fun fetchUpcomingEvents() {
        val db = FirebaseFirestore.getInstance()
        db.collection("upcomingevents")
            .orderBy("formattedDate")
            .get()
            .addOnSuccessListener { result ->
                eventList.clear()
                for (document in result) {
                    val eventName = document.getString("name").toString()
                    val eventDate = document.getString("formattedDate").toString()
                    val eventDesc = document.getString("description").toString()

                    eventList.add("$eventName - $eventDate - $eventDesc")
                }

                val adapter = ArrayAdapter(this, R.layout.list_item_event, eventList)
                eventsListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch events: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}



