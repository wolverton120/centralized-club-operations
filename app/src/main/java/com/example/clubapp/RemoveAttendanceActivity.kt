package com.example.clubapp

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

class RemoveAttendanceActivity : AppCompatActivity() {

    private lateinit var removeAttendanceListView: ListView
    private lateinit var textViewRemoveHeader: TextView
    private lateinit var eventList: MutableList<String>
    private lateinit var selectedEventId: String
    private lateinit var studentName: String
    private lateinit var studentId: String
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val fullText = "Remove Attendance"
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_attendance)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        textViewRemoveHeader = findViewById(R.id.textViewRemoveHeader)
        removeAttendanceListView = findViewById(R.id.removeAttendanceListView)
        eventList = mutableListOf()

        startTextAnimation()
        getStudentName()
        setupRemoveAttendanceButton()
    }

    private fun startTextAnimation() {
        val delay = 100L
        val resetInterval = 12000L

        handler.post(object : Runnable {
            override fun run() {
                textViewRemoveHeader.text = ""
                currentIndex = 0

                handler.postDelayed(object : Runnable {
                    override fun run() {
                        if (currentIndex < fullText.length) {
                            textViewRemoveHeader.text = fullText.substring(0, currentIndex + 1)
                            currentIndex++
                            handler.postDelayed(this, delay)
                        }
                    }
                }, delay)

                handler.postDelayed(this, resetInterval)
            }
        })
    }

    private fun getStudentName() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("students").document(userId).get()
            .addOnSuccessListener { document ->
                studentName = document.getString("name") ?: ""
                studentId = document.getString("id") ?: ""
                fetchRegisteredEvents()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get student name", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchRegisteredEvents() {
        db.collection("eventattendance").get()
            .addOnSuccessListener { result ->
                eventList.clear()
                val eventIdMap = mutableMapOf<String, String>()

                for (document in result) {
                    val studentsArray = document.get("students") as? List<Map<String, String>> ?: listOf()
                    if (studentsArray.any { it["name"] == studentName }) {
                        val eventName = document.getString("name") ?: ""
                        eventList.add(eventName)
                        eventIdMap[eventName] = document.id
                    }
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, eventList)
                removeAttendanceListView.choiceMode = ListView.CHOICE_MODE_SINGLE
                removeAttendanceListView.adapter = adapter

                removeAttendanceListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedEventName = eventList[position]
                    selectedEventId = eventIdMap[selectedEventName] ?: ""
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch events", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupRemoveAttendanceButton() {
        findViewById<Button>(R.id.removeAttendanceButton).setOnClickListener {
            if (!::selectedEventId.isInitialized) {
                Toast.makeText(this, "Please select an event to remove attendance", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("eventattendance").document(selectedEventId)
                .get()
                .addOnSuccessListener { document ->
                    val studentsArray = document.get("students") as? List<Map<String, String>> ?: listOf()
                    val studentData = studentsArray.find { it["name"] == studentName && it["id"] == studentId }

                    if (studentData != null) {
                        db.collection("eventattendance").document(selectedEventId)
                            .update("students", FieldValue.arrayRemove(studentData))
                            .addOnSuccessListener {
                                Toast.makeText(this, "Attendance removed successfully", Toast.LENGTH_SHORT).show()
                                fetchRegisteredEvents()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to remove attendance", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Attendance record not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch event data", Toast.LENGTH_SHORT).show()
                }
        }
    }
}







