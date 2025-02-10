package com.example.clubapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class EditSpecificNoticeActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var submitterEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var noticeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_specific_notice)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        submitterEditText = findViewById(R.id.submitterEditText)
        saveButton = findViewById(R.id.saveButton)

        noticeId = intent.getStringExtra("noticeId") ?: return

        FirebaseFirestore.getInstance().collection("notices")
            .document(noticeId)
            .get()
            .addOnSuccessListener { document ->
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val submitter = document.getString("submitter") ?: ""

                titleEditText.setText(title)
                contentEditText.setText(content)
                submitterEditText.setText(submitter)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load notice", Toast.LENGTH_SHORT).show()
            }

        saveButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedContent = contentEditText.text.toString()
            val updatedSubmitter = submitterEditText.text.toString()
            val updatedTimestamp = FieldValue.serverTimestamp()

            if (updatedTitle.isEmpty() || updatedContent.isEmpty() || updatedSubmitter.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedNotice = hashMapOf(
                "title" to updatedTitle,
                "content" to updatedContent,
                "submitter" to updatedSubmitter,
                "timestamp" to updatedTimestamp
            )

            FirebaseFirestore.getInstance().collection("notices")
                .document(noticeId)
                .update(updatedNotice)
                .addOnSuccessListener {
                    Toast.makeText(this, "Notice updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update notice", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
