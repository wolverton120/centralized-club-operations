package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class teacher_manage : AppCompatActivity() {
    private lateinit var headerTextView: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_manage)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.textView8)
        startTextAnimation()

        val editEventScheduleButton: Button = findViewById(R.id.editEventScheduleButton)
        editEventScheduleButton.setOnClickListener {
            val intent = Intent(this, ManageEventActivity::class.java)
            startActivity(intent)
        }

        val editNotice: Button = findViewById(R.id.editnoticebutton)
        editNotice.setOnClickListener {
            val intent = Intent(this, TeacherManagementActivity::class.java)
            startActivity(intent)
        }

        val editStudent: Button = findViewById(R.id.editStudentProfileTeacherButton)
        editStudent.setOnClickListener {
            val intent = Intent(this, EditStudentListActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startTextAnimation() {
        val text = "MANAGE"
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
