//Class used to handle msgdata in student portal incase I forget wtf this is supposed to do
package com.example.clubapp

data class Message(
    val user: String,
    val message: String,
    val timestamp: String
)