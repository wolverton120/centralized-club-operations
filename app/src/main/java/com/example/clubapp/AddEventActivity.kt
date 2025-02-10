package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEventActivity : AppCompatActivity() {

    private var selectedDateMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val eventName: EditText = findViewById(R.id.eventName)
        val eventDescription: EditText = findViewById(R.id.eventDescription)
        val saveEventButton: Button = findViewById(R.id.saveEventButton)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDateMillis = calendar.timeInMillis
        }

        saveEventButton.setOnClickListener {
            if (selectedDateMillis == 0L) {
                Toast.makeText(this, "Please select a date!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedDate = Date(selectedDateMillis)
            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)

            val name = eventName.text.toString()
            val description = eventDescription.text.toString()

            val event = hashMapOf(
                "formattedDate" to formattedDate,
                "name" to name,
                "description" to description
            )

            FirebaseFirestore.getInstance().collection("upcomingevents")
                .add(event)
                .addOnSuccessListener {
                    Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show()
                }
        }
    }
}




