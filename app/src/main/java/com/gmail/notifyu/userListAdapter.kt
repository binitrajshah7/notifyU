package com.gmail.notifyu

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class userListAdapter(val userList: MutableList<User>) :
    RecyclerView.Adapter<userListAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return viewHolder(view)
    }

    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val email: TextView = view.findViewById(R.id.tvEmail)
        val send: ImageButton = view.findViewById(R.id.btnSend)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val currentUser = userList[position]
        val context = holder.itemView.context
        holder.name.text = currentUser.name
        holder.email.text = currentUser.email

        holder.send.setOnClickListener {
            // creating alert dialog for send button click listener
            val builder = AlertDialog.Builder(context)

            val messageBox: EditText = EditText(context)
            builder.setTitle("Send Notification")
            builder.setView(messageBox)

            // setting positive and negative button
            builder.setNegativeButton("CANCEL", null)
            builder.setPositiveButton("SEND") { dialogInterface, i ->
                val message = messageBox.text.toString()
                sendNotification("Got a message dude!", message, currentUser.id, context)
            }
            builder.show()
        }
    }

    private fun sendNotification(
        title: String,
        text: String,
        receiver_id: String,
        context: Context,
    ) {
        val notification = Notification(text, title, receiver_id)

        FirebaseDatabase
            .getInstance()
            .getReference("Notification")
            .push()
            .setValue(notification)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "✅ Notification Send", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "❌ Failed to Send", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}