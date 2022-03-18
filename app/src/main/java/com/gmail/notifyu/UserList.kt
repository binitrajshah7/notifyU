package com.gmail.notifyu

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.notifyu.databinding.ActivityUserListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserList : AppCompatActivity() {
    lateinit var adapter : userListAdapter
    var userList: MutableList<User> = mutableListOf()

    lateinit var binding: ActivityUserListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "User List"
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setting up recycler view
        getAllUsers()
        adapter = userListAdapter(userList)
        binding.rvUserList.adapter = adapter
        binding.rvUserList.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {

            val userId: String = FirebaseAuth
                .getInstance()
                .currentUser!!.uid
            // clearing token from firebase database as after user logout there is no use of token for the device
            clearToken(userId)

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearToken(userId: String) {
        FirebaseDatabase
            .getInstance()
            .getReference("tokens")
            .child(userId)
            .removeValue()
    }

    private fun getAllUsers() {

        // getting reference to database pointing to user node
        FirebaseDatabase
            .getInstance()
            .getReference("User")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(userSnapshot in snapshot.children){
                            val user = userSnapshot.getValue(User::class.java)
                            userList.add(user!!)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

//            .addChildEventListener(object : ChildEventListener {
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    val user = snapshot.getValue(User::class.java)
//
//                    // checking if user is not null
//                    if(user != null){
//                        userList.add(user)
//                        adapter.notifyDataSetChanged()
//                    }
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//            })
    }
}