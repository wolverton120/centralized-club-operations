package com.example.clubapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class studentwindow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentwindow)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val rootView = findViewById<ConstraintLayout>(R.id.main)
        val studentTitle = findViewById<TextView>(R.id.textView5)

        val drawable: AnimationDrawable = rootView.background as AnimationDrawable
        drawable.setEnterFadeDuration(1500)
        drawable.setExitFadeDuration(2000)
        drawable.start()

        continuouslyAnimateText(studentTitle, "STUDENT CORNER")

        findViewById<Button>(R.id.studentlogin).setOnClickListener {
            startActivity(Intent(this, studentlogin::class.java))
        }

        findViewById<Button>(R.id.studentreg).setOnClickListener {
            startActivity(Intent(this, studentregister::class.java))
        }
    }

    private fun continuouslyAnimateText(textView: TextView, text: String) {
        val handler = Handler()
        var index = 0

        val animate = object : Runnable {
            override fun run() {
                if (index <= text.length) {
                    textView.text = text.substring(0, index)
                    index++
                    handler.postDelayed(this, 150)
                } else {
                    index = 0
                    handler.postDelayed(this, 10000)
                }
            }
        }
        handler.post(animate)
    }
}

