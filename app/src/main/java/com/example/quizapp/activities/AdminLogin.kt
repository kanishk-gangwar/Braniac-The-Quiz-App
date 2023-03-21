package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TokenWatcher
import android.util.Log
import android.widget.Toast
import com.example.quizapp.R
import com.example.quizapp.databinding.ActivityAdminLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminLogin : AppCompatActivity() {

    lateinit var binding : ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val email = binding.adminEmail.text.toString()
        val password = binding.adminpassword.text.toString()

        val db = Firebase.firestore
        binding.adminLoginBtn.setOnClickListener {

            db.collection("Admin").whereEqualTo("AdminEmail",email).whereEqualTo("Adminpassword",password)
                .get()
                .addOnSuccessListener {
                        val intent = Intent(this@AdminLogin, KeyReviewActivity::class.java)
                        startActivity(intent)
                    Toast.makeText(this@AdminLogin,"Successfully Logged in",Toast.LENGTH_SHORT).show()
                    binding.adminEmail.text!!.clear()
                    binding.adminpassword.text!!.clear()
                }

//            docRef.get().addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    val adminEmail = documentSnapshot.getString("AdminEmail")
//                    val adminPassword = documentSnapshot.getString("Adminpassword")
//                    if (adminEmail == email && adminPassword == password) {
//                         Log.d("Email",adminEmail)
//                         Log.d("password",adminPassword)
//                        val intent = Intent(this@AdminLogin, KeyReviewActivity::class.java)
//                        startActivity(intent)
//
//                    }
//                    // Do something with the values
//                } else {
//                    // Document does not exist
//                }
//            }.addOnFailureListener { exception ->
//                // Handle exceptions
//            }

            //R.layout.activity_admin_login
        }
    }
}