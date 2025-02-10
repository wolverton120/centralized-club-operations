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

class EditSpecificEventActivity : AppCompatActivity() {

    private var selectedDateMillis: Long = 0L
    private lateinit var eventNameEditText: EditText
    private lateinit var eventDescriptionEditText: EditText
    private lateinit var calendarView: CalendarView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_specific_event)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        eventNameEditText = findViewById(R.id.eventNameEditText)
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText)
        calendarView = findViewById(R.id.calendarView)
        saveButton = findViewById(R.id.saveButton)

        val eventName = intent.getStringExtra("eventName") ?: return
        eventNameEditText.setText(eventName)

        FirebaseFirestore.getInstance().collection("upcomingevents")
            .whereEqualTo("name", eventName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val event = documents.first()
                val description = event.getString("description") ?: ""
                val date = event.getString("formattedDate") ?: ""

                eventDescriptionEditText.setText(description)

                val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                try {
                    val parsedDate = dateFormat.parse(date)
                    selectedDateMillis = parsedDate?.time ?: 0L
                    calendarView.date = selectedDateMillis
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    selectedDateMillis = calendar.timeInMillis
                }

                saveButton.setOnClickListener {
                    if (selectedDateMillis == 0L) {
                        Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val selectedDate = Date(selectedDateMillis)
                    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate)

                    val updatedName = eventNameEditText.text.toString()
                    val updatedDescription = eventDescriptionEditText.text.toString()

                    val updatedEvent = hashMapOf(
                        "formattedDate" to formattedDate,
                        "name" to updatedName,
                        "description" to updatedDescription
                    ) as MutableMap<String, Any>

                    event.reference.update(updatedEvent)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }
}



