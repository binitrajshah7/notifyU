package com.gmail.notifyu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.gmail.notifyu.databinding.ActivityUserListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserList : AppCompatActivity() {

    lateinit var binding : ActivityUserListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "User List"
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){

            val userId : String = FirebaseAuth
                .getInstance()
                .currentUser!!.uid
            // clearing token from firebase database as after user logout there is no use of token for the device
            clearToken(userId)

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity :: class.java)
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
}