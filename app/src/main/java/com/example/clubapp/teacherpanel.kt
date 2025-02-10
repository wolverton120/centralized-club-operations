package com.example.clubapp

import android.content.Intent
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
import com.example.clubapp.databinding.ActivityTeacherpanelBinding
import com.google.firebase.auth.FirebaseAuth

class teacherpanel : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherpanelBinding

    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherpanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        animateTextLoop(binding.textView16, "TEACHER PANEL", 10000)

        val liststudent = findViewById<Button>(R.id.studentlist)
        liststudent.setOnClickListener {
            val intent = Intent(this, studentlist::class.java)
            startActivity(intent)
        }

        val teacherManage = findViewById<Button>(R.id.teachermanage)
        teacherManage.setOnClickListener {
            val intent = Intent(this, teacher_manage::class.java)
            startActivity(intent)
        }

        val profteach = findViewById<Button>(R.id.teacherprofile)
        profteach.setOnClickListener {
            val intent = Intent(this, teacherprofile::class.java)
            startActivity(intent)
        }

        val teacherNotice = findViewById<Button>(R.id.teachernotice)
        teacherNotice.setOnClickListener {
            val intent = Intent(this, notice_submission::class.java)
            startActivity(intent)
        }

        val feedbackstudent = findViewById<Button>(R.id.studentfeedback)
        feedbackstudent.setOnClickListener {
            val intent = Intent(this, FeedbackListActivity::class.java)
            startActivity(intent)
        }

        val logoutTeacher = findViewById<Button>(R.id.teacherlogout)
        logoutTeacher.setOnClickListener {
            val intent = Intent(this, teacherwindow::class.java)
            startActivity(intent)
            finish()
        }

        val editProfileButton: Button = findViewById(R.id.teacherprofiledit)
        editProfileButton.setOnClickListener {
            val intent = Intent(this, TeacherProfileEditActivity::class.java)
            startActivity(intent)
        }

        /*        binding.teacherlogout.setOnClickListener {
                    auth.signOut()
                } I can't figure out why logout doesn't work for either of the teacher or student panel. So kinda just took a shortcut instead */

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


