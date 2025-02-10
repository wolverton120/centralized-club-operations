package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class studentprofile : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var headerTextView: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentprofile)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.textViewHeader)
        startTextAnimation()

        val nameTextView = findViewById<TextView>(R.id.nameText)
        val idTextView = findViewById<TextView>(R.id.idText)
        val departmentTextView = findViewById<TextView>(R.id.deptText)
        val levelTermTextView = findViewById<TextView>(R.id.levelTermText)
        val emailTextView = findViewById<TextView>(R.id.emailText)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            db.collection("students").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    nameTextView.text = "Name: ${document.getString("name")}"
                    idTextView.text = "ID: ${document.getString("id")}"
                    departmentTextView.text = "Department: ${document.getString("department")}"
                    levelTermTextView.text = "Level/Term: ${document.getString("levelTerm")}"
                    emailTextView.text = "Email: ${document.getString("email")}"
                }
            }.addOnFailureListener {
                nameTextView.text = "Failed to load data!"
            }
        }
    }

    private fun startTextAnimation() {
        val text = "Student Details"
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

