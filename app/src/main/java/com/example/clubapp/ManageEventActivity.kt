package com.example.clubapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ManageEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_event)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val addEventButton: Button = findViewById(R.id.addEventButton)
        val editEventButton: Button = findViewById(R.id.editEventButton)
        val removeEventButton: Button = findViewById(R.id.removeEventButton)
        val eventAttendanceButton: Button = findViewById(R.id.checkEventAttendanceButton)

        addEventButton.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)
        }

        editEventButton.setOnClickListener {
            val intent = Intent(this, EditEventActivity::class.java)
            startActivity(intent)
        }

        removeEventButton.setOnClickListener {
            val intent = Intent(this, RemoveEventActivity::class.java)
            startActivity(intent)
        }

        eventAttendanceButton.setOnClickListener {
            val intent = Intent(this, SelectEventToCheckActivity::class.java)
            startActivity(intent)
        }

        val manageEventText: TextView = findViewById(R.id.manageEventText)
        val animation = ObjectAnimator.ofFloat(manageEventText, "translationX", -1000f, 0f)
        animation.duration = 1000
        animation.start()
    }
}
