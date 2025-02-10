package com.example.clubapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class MessageAdapter(private val context: Context, private val messages: List<Message>) : BaseAdapter() {

    private val db = FirebaseFirestore.getInstance()

    override fun getCount(): Int = messages.size

    override fun getItem(position: Int): Any = messages[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.message_item, parent, false)

        val messageUser = view.findViewById<TextView>(R.id.messageUser)
        val messageText = view.findViewById<TextView>(R.id.messageText)
        val messageTimestamp = view.findViewById<TextView>(R.id.messageTimestamp)
        val repliesText = view.findViewById<TextView>(R.id.repliesText)
        val replyButton = view.findViewById<Button>(R.id.replyButton)

        val message = messages[position]

        messageUser.text = message.user
        messageText.text = message.message
        messageTimestamp.text = message.timestamp

        replyButton.setOnClickListener {
            val intent = Intent(context, ReplyActivity::class.java)
            intent.putExtra("messageText", message.message)
            context.startActivity(intent)
        }

        db.collection("portalreplies")
            .whereEqualTo("message", message.message)
            .get()
            .addOnSuccessListener { documents ->
                val replies = StringBuilder()
                for (doc in documents) {
                    val user = doc.getString("user") ?: "Anonymous"
                    val reply = doc.getString("reply") ?: ""
                    replies.append("$user replied: $reply\n")
                }
                repliesText.text = if (replies.isEmpty()) "" else replies.toString().trim()
            }
            .addOnFailureListener {
                repliesText.text = "Failed to load replies"
            }

        return view
    }
}







