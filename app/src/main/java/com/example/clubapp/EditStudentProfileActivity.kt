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

class EditStudentProfileActivity : AppCompatActivity() {

    private lateinit var studentNameEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var departmentEditText: EditText
    private lateinit var levelTermEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student_profile)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        studentNameEditText = findViewById(R.id.studentNameEditText)
        idEditText = findViewById(R.id.idEditText)
        departmentEditText = findViewById(R.id.departmentEditText)
        levelTermEditText = findViewById(R.id.levelTermEditText)
        emailEditText = findViewById(R.id.emailEditText)
        saveButton = findViewById(R.id.saveButton)

        val currentUser = firebaseAuth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val userId = currentUser.uid

        firestore.collection("students").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    studentNameEditText.setText(document.getString("name"))
                    idEditText.setText(document.getString("id"))
                    departmentEditText.setText(document.getString("department"))
                    levelTermEditText.setText(document.getString("levelTerm"))
                    emailEditText.setText(document.getString("email"))
                } else {
                    Toast.makeText(this, "Student profile not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
                finish()
            }

        saveButton.setOnClickListener {
            val updatedName = studentNameEditText.text.toString()
            val updatedId = idEditText.text.toString()
            val updatedDepartment = departmentEditText.text.toString()
            val updatedLevelTerm = levelTermEditText.text.toString()
            val updatedEmail = emailEditText.text.toString()

            if (updatedName.isBlank() || updatedId.isBlank() || updatedDepartment.isBlank() || updatedLevelTerm.isBlank() || updatedEmail.isBlank()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedProfile = mapOf(
                "name" to updatedName,
                "id" to updatedId,
                "department" to updatedDepartment,
                "levelTerm" to updatedLevelTerm,
                "email" to updatedEmail
            )

            firestore.collection("students").document(userId)
                .update(updatedProfile)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

