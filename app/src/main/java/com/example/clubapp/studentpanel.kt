package com.example.clubapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubapp.databinding.ActivityStudentpanelBinding
import com.google.firebase.auth.FirebaseAuth

class studentpanel : AppCompatActivity() {

    private lateinit var binding: ActivityStudentpanelBinding

    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentpanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        animateTextLoop(binding.textView17, "STUDENT PANEL", 10000)

        binding.studentlogout.setOnClickListener {
            auth.signOut()
        }

/*        val logoutstudent = findViewById<Button>(R.id.studentlogout)
        logoutstudent.setOnClickListener {
            auth.signOut()
        } I can't figure out why logout doesn't work for either of the teacher or student panel. So kinda just took a shortcut instead */

        val logoutStudent = findViewById<Button>(R.id.studentlogout)
        logoutStudent.setOnClickListener {
            val intent = Intent(this, studentwindow::class.java)
            startActivity(intent)
            finish()
        }

        val profilestudent = findViewById<Button>(R.id.studentprofile)
        profilestudent.setOnClickListener {
            val intent = Intent(this, studentprofile::class.java)
            startActivity(intent)
        }

        val noticestudent = findViewById<Button>(R.id.studentnotice)
        noticestudent.setOnClickListener {
            val intent = Intent(this, studentnotice::class.java)
            startActivity(intent)
        }

       /* val galleryclub = findViewById<Button>(R.id.gallery)
        galleryclub.setOnClickListener {
            val intent = Intent(this, clubgallery::class.java)
            startActivity(intent)
        } old button to failed gallery implementation, now reworked to baiustwebsite */

        val submitFeedback = findViewById<Button>(R.id.studentfeedbacksubmit)
        submitFeedback.setOnClickListener {
            val intent = Intent(this, FeedbackSubmissionActivity::class.java)
            startActivity(intent)
        }

       /* val upcomingEventsButton: Button = findViewById(R.id.upcomingEventsButton)

        upcomingEventsButton.setOnClickListener {
            val intent = Intent(this, StudentUpcomingEventsActivity::class.java)
            startActivity(intent)
        } */

        val editProfileButton: Button = findViewById(R.id.editStudentProfileStudentButton)
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditStudentProfileActivity::class.java)
            startActivity(intent)
        }

        val studentPortalButton: Button = findViewById(R.id.studentPortalButton)
        studentPortalButton.setOnClickListener {
            val intent = Intent(this, StudentPortalActivity::class.java)
            startActivity(intent)
        }

        val upcomingEventsButton = findViewById<Button>(R.id.upcomingEventsButton)

        upcomingEventsButton.setOnClickListener {
            val intent = Intent(this, EventSelectionActivity::class.java)
            startActivity(intent)
        }

        val baiustWebsiteButton = findViewById<Button>(R.id.baiustwebsite)
        baiustWebsiteButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://iumss.baiust.ac.bd"))
            startActivity(browserIntent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun animateTextLoop(textView: TextView, text: String, delay: Long) {
        val handler = Handler(Looper.getMainLooper())
        var index = 0
        val animationRunnable = object : Runnable {
            override fun run() {
                if (index < text.length) {
                    textView.text = text.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, 150)
                } else {
                    handler.postDelayed({
                        index = 0
                        textView.text = ""
                        handler.post(this)
                    }, delay)
                }
            }
        }
        handler.post(animationRunnable)
    }
}


