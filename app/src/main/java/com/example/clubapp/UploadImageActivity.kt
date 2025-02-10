//Unused class after club gallery function removed
package com.example.clubapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Locale

class UploadImageActivity : AppCompatActivity() {

    private lateinit var selectedImageUri: Uri
    private val storageReference = FirebaseStorage.getInstance().reference.child("ClubGallery")

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        val uploadButton = findViewById<Button>(R.id.uploadImageButton)
        val imageView = findViewById<ImageView>(R.id.imageView)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data!!.data!!
                imageView.setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(intent)
        }

        uploadButton.setOnClickListener {
            if (::selectedImageUri.isInitialized) {
                val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                val now = java.util.Date()
                val fileName = formatter.format(now)
                val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

                    storageReference.putFile(selectedImageUri)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
