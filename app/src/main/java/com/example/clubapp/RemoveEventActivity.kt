package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RemoveEventActivity : AppCompatActivity() {

    private lateinit var eventRadioGroup: RadioGroup
    private lateinit var removeSelectedEventButton: Button
    private lateinit var eventList: MutableList<String>
    private var selectedEventName: String? = null
    private lateinit var headerTextView: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_event)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.removeEventHeaderTextView)
        startTextAnimation()

        eventRadioGroup = findViewById(R.id.eventRadioGroup)
        removeSelectedEventButton = findViewById(R.id.removeSelectedEventButton)
        eventList = mutableListOf()

        FirebaseFirestore.getInstance().collection("upcomingevents")
            .orderBy("formattedDate")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val eventName = document.getString("name") ?: ""
                    eventList.add(eventName)

                    val radioButton = RadioButton(this)
                    radioButton.text = eventName
                    radioButton.textSize = 18f
                    eventRadioGroup.addView(radioButton)
                }
            }

        eventRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedEventName = radioButton.text.toString()
        }

        removeSelectedEventButton.setOnClickListener {
            if (selectedEventName != null) {
                FirebaseFirestore.getInstance().collection("upcomingevents")
                    .whereEqualTo("name", selectedEventName)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        for (document in documents) {
                            document.reference.delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Event removed", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to remove event", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
            } else {
                Toast.makeText(this, "Please select an event to remove", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTextAnimation() {
        val text = "Remove Event"
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



