//This should be named edit notice, but its named teacher management. Why? Idk lmao I was sleep deprived working on this part at 4am I guess
package com.example.clubapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class TeacherManagementActivity : AppCompatActivity() {

    private lateinit var noticeListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_management)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        noticeListView = findViewById(R.id.noticeListView)

        val notices = mutableListOf<String>()
        val noticeIds = mutableListOf<String>()

        FirebaseFirestore.getInstance().collection("notices")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val title = document.getString("title") ?: "No title"
                    notices.add(title)
                    noticeIds.add(document.id)
                }

                val adapter = ArrayAdapter(
                    this,
                    R.layout.list_item_notice,
                    notices
                )
                noticeListView.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load notices", Toast.LENGTH_SHORT).show()
            }

        noticeListView.setOnItemClickListener { _, _, position, _ ->
            val selectedNoticeId = noticeIds[position]
            val intent = Intent(this, EditSpecificNoticeActivity::class.java)
            intent.putExtra("noticeId", selectedNoticeId)
            startActivity(intent)
        }
    }
}

