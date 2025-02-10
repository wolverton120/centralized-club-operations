package com.example.clubapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        setContentView(binding.root)

        val rootView = findViewById<ConstraintLayout>(R.id.main)
        val textViewWelcome = findViewById<TextView>(R.id.textView2)
        val textViewQuestion = findViewById<TextView>(R.id.textView)

        val drawable: AnimationDrawable = rootView.background as AnimationDrawable
        drawable.setEnterFadeDuration(1500)
        drawable.setExitFadeDuration(2000)
        drawable.start()

        val welcomeText = "Welcome to CCO!"
        val questionText = "Are you a teacher or\n          a student?"

        startLoopingTextAnimation(textViewWelcome, welcomeText, textViewQuestion, questionText)

        val buttonteacher = findViewById<Button>(R.id.buttonteacher)
        buttonteacher.setOnClickListener {
            val intent = Intent(this, teacherwindow::class.java)
            startActivity(intent)
        }

        val buttonstudent = findViewById<Button>(R.id.buttonstudent)
        buttonstudent.setOnClickListener {
            val intent = Intent(this, studentwindow::class.java)
            startActivity(intent)
        }

        val buttonDev = findViewById<Button>(R.id.button_about_dev)
        buttonDev.setOnClickListener {
            showAboutDevPopup()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startLoopingTextAnimation(
        textViewWelcome: TextView,
        welcomeText: String,
        textViewQuestion: TextView,
        questionText: String
    ) {
        fun loopAnimation() {
            displayTextSequentially(textViewWelcome, welcomeText) {
                displayTextSequentially(textViewQuestion, questionText) {
                    handler.postDelayed({ loopAnimation() }, 10000L)
                }
            }
        }
        loopAnimation()
    }

    private fun displayTextSequentially(textView: TextView, text: String, onComplete: (() -> Unit)? = null) {
        textView.text = ""
        val handler = Handler(Looper.getMainLooper())

        for (i in text.indices) {
            handler.postDelayed({
                textView.text = text.substring(0, i + 1)
                if (i == text.length - 1) {
                    onComplete?.invoke()
                }
            }, i * 80L)
        }
    }

    private fun showAboutDevPopup() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_about_dev)

        val textAboutDev = dialog.findViewById<TextView>(R.id.text_about_dev)
        val buttonOk = dialog.findViewById<Button>(R.id.button_ok)

        dialog.setCancelable(false)

        val aboutText = "Made by Sadman Adib, CSE 16th batch, BAIUST. This was developed as a part of the semester project during 3-1."
        displayTextSequentially(textAboutDev, aboutText)

        buttonOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}


