package com.gmail.notifyu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gmail.notifyu.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding

    // if user have already signedIn in previous activity
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
            // finishing current login activity & moving to userList activity
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Login"

//        // Write a message to the database
//        val database = Firebase.database
//        val myRef = database.getReference("message")
//        myRef.setValue("Hello, World!")

        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show()
            }else{
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                login(email, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent : Intent = Intent(this, registerActivity :: class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String) {
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val intent = Intent(this, UserList::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    // finishes all previous screens
                    finishAffinity()
                }
                else{
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}