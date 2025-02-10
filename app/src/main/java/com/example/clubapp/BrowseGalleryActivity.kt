//Unused class after club gallery function removed
package com.example.clubapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage

class BrowseGalleryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private val imageUrls = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_gallery)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ImageAdapter(imageUrls)
        recyclerView.adapter = adapter

        fetchImages()
    }

    private fun fetchImages() {
        val storageRef = FirebaseStorage.getInstance().reference.child("ClubGallery")
        storageRef.listAll().addOnSuccessListener { listResult ->
            for (fileRef in listResult.items) {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUrls.add(uri.toString())
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch image URLs.", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to fetch images from Firebase.", Toast.LENGTH_SHORT).show()
        }
    }
}
