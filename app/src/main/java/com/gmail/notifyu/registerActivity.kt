package com.gmail.notifyu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gmail.notifyu.databinding.ActivityLoginBinding
import com.gmail.notifyu.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class registerActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "landed on register page", Toast.LENGTH_SHORT).show()

        binding.btnRegister.setOnClickListener{
            if(binding.etName.text.isEmpty() ||binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()){
                Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show()
            }else{
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val name = binding.etName.text.toString()

                register(name, email, password)
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val userId : String = FirebaseAuth.getInstance().currentUser!!.uid
                    storeUser(name, email, userId)

                    val intent = Intent(this, UserList::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                else{
                    Toast.makeText(this, "Invalid email address!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun storeUser(name: String, email: String, id: String) {
        val newUser = User(name, email, id)

        FirebaseDatabase
            .getInstance()
            .getReference("User")
            .push()
            .setValue(newUser)
            .addOnCompleteListener { task->
                // if the user don't have internet connection
                if(task.isSuccessful){
                    Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
                }

            }
    }
}