package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubapp.databinding.ActivityStudentloginBinding

class studentlogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStudentloginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        animateText(binding.textView18, "STUDENT LOGIN")

        binding.studentlogin.setOnClickListener {
            val email = binding.studentemailog.text.toString()
            val password = binding.studentpasswordlog.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please check if you've written the email correctly or left a space at the end", Toast.LENGTH_LONG).show()
            } else {
                MainActivity.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, studentpanel::class.java))
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        findViewById<Button>(R.id.button18).setOnClickListener {
            startActivity(Intent(this, studentregister::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun animateText(textView: TextView, text: String) {
        textView.text = ""
        val handler = Handler(Looper.getMainLooper())
        var index = 0
        handler.post(object : Runnable {
            override fun run() {
                if (index < text.length) {
                    textView.text = textView.text.toString() + text[index]
                    index++
                    handler.postDelayed(this, 150)
                }
            }
        })
    }
}


