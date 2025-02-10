package com.example.clubapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class teacherprofile : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var headerTextView: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacherprofile)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        headerTextView = findViewById(R.id.textViewHeader)
        startTextAnimation()

        val teachernameTextView = findViewById<TextView>(R.id.teachernameText)
        val teacherdesgView = findViewById<TextView>(R.id.teacherdesigtext)
        val teacheremailView = findViewById<TextView>(R.id.teacheremailText)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            db.collection("teachers").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    teachernameTextView.text = "Name: ${document.getString("teachername")}"
                    teacherdesgView.text = "Designation: ${document.getString("teacherdesignation")}"
                    teacheremailView.text = "Email: ${document.getString("teacheremail")}"
                }
            }.addOnFailureListener {
                teachernameTextView.text = "Failed to load data!"
            }
        }
    }

    private fun startTextAnimation() {
        val text = "Teacher Details"
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
