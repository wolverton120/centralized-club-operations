package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherProfileEditActivity : AppCompatActivity() {

    private lateinit var teacherNameEditText: EditText
    private lateinit var teacherEmailEditText: EditText
    private lateinit var teacherDesignationEditText: EditText
    private lateinit var saveChangesButton: Button

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_profile_edit)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        teacherNameEditText = findViewById(R.id.teacherNameEditText)
        teacherEmailEditText = findViewById(R.id.teacherEmailEditText)
        teacherDesignationEditText = findViewById(R.id.teacherDesignationEditText)
        saveChangesButton = findViewById(R.id.saveChangesButton)

        val user = firebaseAuth.currentUser
        if (user != null) {
            val teacherId = user.uid
            fetchTeacherDetails(teacherId)
        }

        saveChangesButton.setOnClickListener {
            val updatedName = teacherNameEditText.text.toString()
            val updatedEmail = teacherEmailEditText.text.toString()
            val updatedDesignation = teacherDesignationEditText.text.toString()

            if (updatedName.isNotEmpty() && updatedEmail.isNotEmpty() && updatedDesignation.isNotEmpty()) {
                val updatedTeacherData = hashMapOf<String, Any>(
                    "teachername" to updatedName,
                    "teacheremail" to updatedEmail,
                    "teacherdesignation" to updatedDesignation
                )

                saveTeacherDetails(updatedTeacherData)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTeacherDetails(teacherId: String) {
        firestore.collection("teachers").document(teacherId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    teacherNameEditText.setText(document.getString("teachername"))
                    teacherEmailEditText.setText(document.getString("teacheremail"))
                    teacherDesignationEditText.setText(document.getString("teacherdesignation"))
                } else {
                    Toast.makeText(this, "No teacher found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching teacher details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveTeacherDetails(updatedTeacherData: HashMap<String, Any>) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val teacherId = user.uid
            firestore.collection("teachers").document(teacherId)
                .set(updatedTeacherData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error saving profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

