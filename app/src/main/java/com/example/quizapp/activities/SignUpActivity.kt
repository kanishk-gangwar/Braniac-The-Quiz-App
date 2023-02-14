package com.example.quizapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.R
import com.example.quizapp.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var SignUpEmail : EditText
    private lateinit var SignUpName : EditText
    private lateinit var SignUppassword : EditText
    private lateinit var SignUpCpassword : EditText
    lateinit var firebaseAuth : FirebaseAuth
    private lateinit var SignUpBtn : Button
    private lateinit var loginBtn : TextView
    private var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SignUpEmail = findViewById(R.id.SignUpEmail)
        SignUpName = findViewById(R.id.SignUpName)
        SignUppassword = findViewById(R.id.SignUppassword)
        SignUpCpassword = findViewById(R.id.SignUpCpassword)
        SignUpBtn = findViewById(R.id.SignUpbtn)
        loginBtn = findViewById(R.id.LoginBtn)
        firebaseAuth = FirebaseAuth.getInstance()



        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        SignUpBtn.setOnClickListener {
            signUpUser()
            savUserData()
        }
    }

    private fun savUserData() {
        val name  = SignUpName.text.toString()
        val email  = SignUpEmail.text.toString()
        val password = SignUppassword.text.toString()


        val userMap = hashMapOf(
            "Name" to name,
            "Email" to email,
            "Password" to password
        )
        val uid = firebaseAuth.currentUser?.uid

        if (uid != null) {
            db.collection("User").document(uid).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"User SuccessFully Added",Toast.LENGTH_SHORT).show()
                    SignUpName.text.clear()
                    SignUpEmail.text.clear()
                    SignUppassword.text.clear()
                    SignUpCpassword.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed!!",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun signUpUser(){

         val email = SignUpEmail.text.toString()
         val password = SignUppassword.text.toString()
         val ConfirmPassword = SignUpCpassword.text.toString()
         val Name = SignUpName.text.toString()


        if(email.isBlank() || password.isBlank() || ConfirmPassword.isBlank() || Name.isBlank()){
            Toast.makeText(this,"Details can't be blank",Toast.LENGTH_SHORT).show()
            return
        }
        if(password != ConfirmPassword){
            Toast.makeText(this,"Password and Confirm Password doesn't match",Toast.LENGTH_SHORT).show()
            return
        }


        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful){
                  //  Toast.makeText(this,"User created successfully",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                else{
                   // Toast.makeText(this,"Error Creating User",Toast.LENGTH_SHORT).show()

                }
            }
    }
}