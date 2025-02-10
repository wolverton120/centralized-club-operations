package com.example.clubapp

import android.content.Intent
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
import com.google.firebase.firestore.FirebaseFirestore

class SelectEventToCheckActivity : AppCompatActivity() {

    private lateinit var eventListView: ListView
    private lateinit var selectEventButton: Button
    private lateinit var removeRecordButton: Button
    private val eventList = mutableListOf<String>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var selectedEventId: String
    private lateinit var selectEventTextView: TextView
    private val eventIdMap = mutableMapOf<String, String>()

    private val handler = Handler(Looper.getMainLooper())
    private val fullText = "Select Event to Check"
    private var textIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_event_to_check)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        eventListView = findViewById(R.id.selectEventListView)
        selectEventButton = findViewById(R.id.selectEventButton)
        removeRecordButton = findViewById(R.id.removeRecordButton)
        selectEventTextView = findViewById(R.id.selectEventTextView)

        fetchUpcomingEvents()
        setupEventSelectButton()
        setupRemoveRecordButton()
        animateText()
    }

    private fun fetchUpcomingEvents() {
        db.collection("upcomingevents")
            .orderBy("formattedDate")
            .get()
            .addOnSuccessListener { result ->
                eventList.clear()
                eventIdMap.clear()
                for (document in result) {
                    val eventName = document.getString("name").toString()
                    eventList.add(eventName)
                    eventIdMap[eventName] = document.id
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, eventList)
                eventListView.adapter = adapter
                eventListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedEventName = eventList[position]
                    selectedEventId = eventIdMap[selectedEventName] ?: ""
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch events", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupEventSelectButton() {
        selectEventButton.setOnClickListener {
            if (::selectedEventId.isInitialized) {
                val intent = Intent(this, AttendanceListActivity::class.java)
                intent.putExtra("selectedEventId", selectedEventId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select an event", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRemoveRecordButton() {
        removeRecordButton.setOnClickListener {
            if (::selectedEventId.isInitialized) {
                db.collection("eventattendance")
                    .document(selectedEventId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Event removed successfully!", Toast.LENGTH_SHORT).show()
                        fetchUpcomingEvents()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to remove event", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please select an event to remove", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun animateText() {
        textIndex = 0
        selectEventTextView.text = ""

        val textAnimator = object : Runnable {
            override fun run() {
                if (textIndex < fullText.length) {
                    selectEventTextView.text = fullText.substring(0, textIndex + 1)
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



