package com.example.clubapp

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class StudentAttendEventActivity : AppCompatActivity() {

    private lateinit var attendEventsListView: ListView
    private lateinit var eventList: MutableList<String>
    private lateinit var selectedEvent: String
    private lateinit var selectEventButton: Button
    private lateinit var textViewAttendHeader: TextView

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val fullText = "Select Event to Attend"
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_attend_event)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        textViewAttendHeader = findViewById(R.id.textViewAttendHeader)
        attendEventsListView = findViewById(R.id.attendEventsListView)
        selectEventButton = findViewById(R.id.selectEventButton)
        eventList = mutableListOf()

        startTextAnimation()
        fetchUpcomingEvents()

        attendEventsListView.setOnItemClickListener { _, _, position, _ ->
            selectedEvent = eventList[position]
            Toast.makeText(this, "Selected: $selectedEvent", Toast.LENGTH_SHORT).show()
        }

        selectEventButton.setOnClickListener {
            if (::selectedEvent.isInitialized) {
                showConfirmationDialog()
            } else {
                Toast.makeText(this, "Please select an event!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTextAnimation() {
        val delay = 100L
        val resetInterval = 12000L

        handler.post(object : Runnable {
            override fun run() {
                textViewAttendHeader.text = ""
                currentIndex = 0

                handler.postDelayed(object : Runnable {
                    override fun run() {
                        if (currentIndex < fullText.length) {
                            textViewAttendHeader.text = fullText.substring(0, currentIndex + 1)
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
        db.collection("upcomingevents").orderBy("formattedDate").get()
            .addOnSuccessListener { result ->
                eventList.clear()
                for (document in result) {
                    val eventName = document.getString("name") ?: "Unnamed Event"
                    eventList.add(eventName)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, eventList)
                attendEventsListView.adapter = adapter
            }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Attendance")
            .setMessage("Do you want to participate in this event?")
            .setPositiveButton("Yes") { _, _ -> registerAttendance() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun registerAttendance() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("students").document(user.uid).get()
            .addOnSuccessListener { studentDoc ->
                if (!studentDoc.exists()) {
                    Toast.makeText(this, "Student record not found!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val studentName = studentDoc.getString("name") ?: "Unknown Student"
                val studentId = studentDoc.getString("id") ?: "Unknown ID"

                val studentData = mapOf("id" to studentId, "name" to studentName)

                db.collection("upcomingevents")
                    .whereEqualTo("name", selectedEvent)
                    .get()
                    .addOnSuccessListener { eventDocs ->
                        if (eventDocs.isEmpty) {
                            Toast.makeText(this, "Event not found!", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        val eventData = eventDocs.documents[0].data ?: return@addOnSuccessListener
                        val eventId = eventDocs.documents[0].id

                        val eventAttendanceRef = db.collection("eventattendance").document(eventId)

                        eventAttendanceRef.get().addOnSuccessListener { attendanceDoc ->
                            if (!attendanceDoc.exists()) {
                                val attendanceData = HashMap(eventData)
                                attendanceData["students"] = listOf(studentData)

                                eventAttendanceRef.set(attendanceData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Attendance Recorded!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to record attendance!", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                eventAttendanceRef.update("students", FieldValue.arrayUnion(studentData))
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Attendance Recorded!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to record attendance!", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching student details!", Toast.LENGTH_SHORT).show()
            }
    }
}

