package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubapp.databinding.ActivityTeacherresgisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class teacherresgister : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherresgisterBinding

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeacherresgisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        animateText(findViewById(R.id.teacherRegisterTitle), "    TEACHER\nREGISTRATION")

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val designation = binding.desigEditText.text.toString()
            val teacherid = binding.teacherIdEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || designation.isEmpty() || teacherid.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please check if you've written the email correctly or left a space at the end", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            db.collection("teacherids").document("teacherid")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val storedTeacherId = document.getString("teacherid")

                        if (teacherid == storedTeacherId) {
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = hashMapOf(
                                            "teachername" to name,
                                            "teacherdesignation" to designation,
                                            "teacheremail" to email
                                        )

                                        val uid = task.result.user?.uid
                                        if (uid != null) {
                                            db.collection("teachers").document(uid).set(user)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        this,
                                                        "Registration Successful!",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    startActivity(Intent(this, teacherlogin2::class.java))
                                                    finish()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        this,
                                                        "Failed to save details: ${e.localizedMessage}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        }
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                                }
                        } else {
                            Toast.makeText(this, "Teacher ID does not match", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch teacher ID from Firebase", Toast.LENGTH_LONG).show()
                }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun animateText(textView: TextView, text: String) {
        val handler = Handler()
        var index = 0

        val animate = object : Runnable {
            override fun run() {
                if (index <= text.length) {
                    textView.text = text.substring(0, index)
                    index++
                    handler.postDelayed(this, 150)
                }
            }
        }
        handler.post(animate)
    }
}



