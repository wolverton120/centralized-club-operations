package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditStudentListActivity : AppCompatActivity() {

    private lateinit var studentListView: ListView
    private val studentNames = mutableListOf<String>()
    private val studentIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student_list)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        studentListView = findViewById(R.id.studentListView)

        FirebaseFirestore.getInstance().collection("students")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("name") ?: "Unknown"
                    val id = document.id
                    studentNames.add(name)
                    studentIds.add(id)
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentNames)
                studentListView.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load students", Toast.LENGTH_SHORT).show()
            }

        studentListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedStudentId = studentIds[position]
            val intent = Intent(this, EditSpecificStudentActivity::class.java)
            intent.putExtra("studentId", selectedStudentId)
            startActivity(intent)
        }
    }
}
