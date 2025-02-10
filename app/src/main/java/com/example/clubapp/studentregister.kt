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
import com.example.clubapp.databinding.ActivityStudentregisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class studentregister : AppCompatActivity() {

    private lateinit var binding: ActivityStudentregisterBinding

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentregisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        supportActionBar?.title = "Student Register"

        animateText(findViewById(R.id.textView7), "APPLY FOR A CLUB")

        binding.apply.setOnClickListener {
            val email = binding.studentemailreg.text.toString()
            val password = binding.studentpassreg.text.toString()
            val name = binding.studentname.text.toString()
            val id = binding.studentid.text.toString()
            val department = binding.studentdept.text.toString()
            val levelTerm = binding.studentlevelterm.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || id.isEmpty() || department.isEmpty() || levelTerm.isEmpty()) {
                Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_LONG).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please check if you've written the email correctly or left a space at the end", Toast.LENGTH_LONG).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = hashMapOf(
                                "name" to name,
                                "id" to id,
                                "department" to department,
                                "levelTerm" to levelTerm,
                                "email" to email
                            )

                            val uid = task.result.user?.uid
                            if (uid != null) {
                                db.collection("students").document(uid).set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this, studentlogin::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to save details: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
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
