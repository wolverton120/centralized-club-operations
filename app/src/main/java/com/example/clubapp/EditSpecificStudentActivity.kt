package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class EditSpecificStudentActivity : AppCompatActivity() {

    private lateinit var idEditText: EditText
    private lateinit var departmentEditText: EditText
    private lateinit var levelTermEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var studentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_specific_student)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        idEditText = findViewById(R.id.idEditText)
        departmentEditText = findViewById(R.id.departmentEditText)
        levelTermEditText = findViewById(R.id.levelTermEditText)
        saveButton = findViewById(R.id.saveButton)

        studentId = intent.getStringExtra("studentId") ?: return

        FirebaseFirestore.getInstance().collection("students")
            .document(studentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    idEditText.setText(document.getString("id"))
                    departmentEditText.setText(document.getString("department"))
                    levelTermEditText.setText(document.getString("levelTerm"))
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load student data", Toast.LENGTH_SHORT).show()
            }

        saveButton.setOnClickListener {
            val updatedId = idEditText.text.toString()
            val updatedDepartment = departmentEditText.text.toString()
            val updatedLevelTerm = levelTermEditText.text.toString()

            if (updatedId.isBlank() || updatedDepartment.isBlank() || updatedLevelTerm.isBlank()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = mapOf(
                "id" to updatedId,
                "department" to updatedDepartment,
                "levelTerm" to updatedLevelTerm,
                "timestamp" to Date()
            )

            FirebaseFirestore.getInstance().collection("students")
                .document(studentId)
                .update(updatedStudent)
                .addOnSuccessListener {
                    Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
