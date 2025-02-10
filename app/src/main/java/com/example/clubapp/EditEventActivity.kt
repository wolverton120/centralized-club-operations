package com.example.clubapp

import android.content.Intent
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

class EditEventActivity : AppCompatActivity() {

    private lateinit var eventRadioGroup: RadioGroup
    private lateinit var editSelectedEventButton: Button
    private lateinit var eventList: MutableList<String>
    private var selectedEventName: String? = null
    private lateinit var headerTextView: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.editEventHeaderTextView)
        startTextAnimation()

        eventRadioGroup = findViewById(R.id.eventRadioGroup)
        editSelectedEventButton = findViewById(R.id.editSelectedEventButton)
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

        editSelectedEventButton.setOnClickListener {
            if (selectedEventName != null) {
                val intent = Intent(this, EditSpecificEventActivity::class.java)
                intent.putExtra("eventName", selectedEventName)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select an event to edit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTextAnimation() {
        val text = "Edit Event"
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




