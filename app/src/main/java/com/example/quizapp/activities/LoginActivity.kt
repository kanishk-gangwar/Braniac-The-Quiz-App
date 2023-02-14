package com.example.quizapp.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.quizapp.R
import com.example.quizapp.activities.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var SignUpBtn : TextView
    private lateinit var LogInBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Email = findViewById(R.id.etEmail)
        Password = findViewById(R.id.etpassword)
        LogInBtn = findViewById(R.id.LoginBtn)
        SignUpBtn = findViewById(R.id.SignUpbtn)
        firebaseAuth = FirebaseAuth.getInstance()

        SignUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        LogInBtn.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()
            val custompgbar = Dialog(this)
            custompgbar.setContentView(R.layout.custompgbar)
            custompgbar.setCanceledOnTouchOutside(false)
            custompgbar.show()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Details can't be blank", Toast.LENGTH_SHORT).show()
                custompgbar.dismiss()

            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Email.text.clear()
                        Password.text.clear()
                        val intent = Intent(this, MainActivity::class.java)
                        custompgbar.dismiss()
                        startActivity(intent)
                        Toast.makeText(this, "Logged In successfully", Toast.LENGTH_SHORT).show()
                        finish()

                    }
                    else{
                        Toast.makeText(this,"Error Logging In Or Details don't exist", Toast.LENGTH_SHORT).show()
                        Email.text.clear()
                        Password.text.clear()
                        custompgbar.dismiss()

                    }
                }

            }
        }
    }
}