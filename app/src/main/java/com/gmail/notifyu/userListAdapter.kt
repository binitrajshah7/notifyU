package com.gmail.notifyu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class userListAdapter(val userList : MutableList<User>) : RecyclerView.Adapter<userListAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return viewHolder(view)
    }

    class viewHolder(view : View): RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.tvName)
        val email : TextView = view.findViewById(R.id.tvEmail)
        val send : ImageButton = view.findViewById(R.id.btnSend)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentUser = userList[position]

        holder.name.text = currentUser.name
        holder.email.text = currentUser.email

        holder.send.setOnClickListener{

        }
    }
    override fun getItemCount(): Int {
        return userList.size
    }
}