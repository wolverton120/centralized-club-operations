package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AttendanceListActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var selectedEventId: String
    private lateinit var eventDescriptionTextView: TextView
    private lateinit var eventDateTextView: TextView
    private lateinit var eventNameTextView: TextView
    private lateinit var attendeesTextView: TextView
    private lateinit var attendanceHeaderTextView: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val fullText = "Attendance List"
    private var textIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_list)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        selectedEventId = intent.getStringExtra("selectedEventId") ?: ""
        eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView)
        eventDateTextView = findViewById(R.id.eventDateTextView)
        eventNameTextView = findViewById(R.id.eventNameTextView)
        attendeesTextView = findViewById(R.id.attendeesTextView)
        attendanceHeaderTextView = findViewById(R.id.attendanceHeaderTextView)

        fetchEventDetails()
        animateText()
    }

    private fun fetchEventDetails() {
        db.collection("eventattendance").document(selectedEventId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val eventName = document.getString("name") ?: "Unknown Event"
                    val eventDescription = document.getString("description") ?: "No description available"
                    val eventDate = document.getString("formattedDate") ?: "Unknown date"
                    val attendees = document.get("students") as? List<Map<String, String>> ?: emptyList()

                    eventNameTextView.text = "Name: $eventName"
                    eventDescriptionTextView.text = "Description: $eventDescription"
                    eventDateTextView.text = "Date: $eventDate"

                    if (attendees.isNotEmpty()) {
                        val attendeesList = StringBuilder("")
                        for (attendee in attendees) {
                            attendeesList.append("${attendee["name"]} (ID: ${attendee["id"]})\n")
                        }
                        attendeesTextView.text = attendeesList.toString()
                    } else {
                        hideEventDetails()
                    }
                } else {
                    hideEventDetails()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch attendance", Toast.LENGTH_SHORT).show()
            }
    }

    private fun hideEventDetails() {
        eventNameTextView.visibility = View.GONE
        eventDescriptionTextView.visibility = View.GONE
        eventDateTextView.visibility = View.GONE
        attendeesTextView.visibility = View.GONE

        attendeesTextView.visibility = View.VISIBLE
        attendeesTextView.text = "There's currently no attendance record for this event."
    }

    private fun animateText() {
        textIndex = 0
        attendanceHeaderTextView.text = ""

        val textAnimator = object : Runnable {
            override fun run() {
                if (textIndex < fullText.length) {
                    attendanceHeaderTextView.text = fullText.substring(0, textIndex + 1)
                    textIndex++
                    handler.postDelayed(this, 100)
                } else {
                    handler.postDelayed({ animateText() }, 12000)
                }
            }
        }
        handler.post(textAnimator)
    }
}




