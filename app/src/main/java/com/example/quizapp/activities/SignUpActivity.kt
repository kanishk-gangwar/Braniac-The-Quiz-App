package com.example.quizapp.activities

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.R
import com.example.quizapp.activities.LoginActivity
import com.example.quizapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var SignUpEmail : EditText
    private lateinit var SignUpName : EditText
    private lateinit var SignUppassword : EditText
    private lateinit var SignUpCpassword : EditText
    lateinit var firebaseAuth : FirebaseAuth
    private lateinit var SignUpBtn : Button
    private lateinit var loginBtn : TextView
    private var db = Firebase.firestore
    private lateinit var binding: ActivitySignUpBinding


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



        passwordFocusListener()

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

    private fun passwordFocusListener() {
        SignUppassword.setOnFocusChangeListener { _, focused ->
            if (!focused){
                passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {

        val passwordText = SignUppassword.text.toString()
        if (passwordText.length < 6){
            return "Minimum 6 Characters Password Required"
        }

        return null

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

        val auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser

            if (user != null) {
                // User is signed in
                // Call a function to update the count
                updateCount()
            }
        }


        db.collection("User").document(email).set(userMap)
            .addOnSuccessListener {
           //     Toast.makeText(this,"User SuccessFully Added",Toast.LENGTH_SHORT).show()
                SignUpName.text.clear()
                SignUpEmail.text.clear()
                SignUppassword.text.clear()
                SignUpCpassword.text.clear()

            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed!!",Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateCount() {
        val db = FirebaseFirestore.getInstance()
        val usersCollectionRef = db.collection("User")

        usersCollectionRef
            .document("count")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val count = documentSnapshot.getLong("count") ?: 0
                usersCollectionRef
                    .document("count")
                    .set(mapOf("count" to count + 1))
            }
    }

    private fun signUpUser(){
        val custompgbar = Dialog(this)
        custompgbar.setContentView(R.layout.custompgbar)
        custompgbar.setCanceledOnTouchOutside(false)
        custompgbar.show()

         val email = SignUpEmail.text.toString()
         val password = SignUppassword.text.toString()
         val ConfirmPassword = SignUpCpassword.text.toString()
         val Name = SignUpName.text.toString()


        if(email.isBlank() || password.isBlank() || ConfirmPassword.isBlank() || Name.isBlank()){
            Toast.makeText(this,"Details can't be blank",Toast.LENGTH_SHORT).show()
            custompgbar.dismiss()
            return
        }
        if(password != ConfirmPassword){
            Toast.makeText(this,"Password and Confirm Password doesn't match",Toast.LENGTH_SHORT).show()
            custompgbar.dismiss()
            return
        }


        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful){
                    checkEmailExist()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    custompgbar.dismiss()
                    finish()

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                custompgbar.dismiss()
            }
    }

    private fun checkEmailExist() {
        val custompgbar = Dialog(this)
        custompgbar.setContentView(R.layout.custompgbar)
        custompgbar.setCanceledOnTouchOutside(false)
        custompgbar.show()

        val email = firebaseAuth.currentUser!!.email
        val auth = FirebaseAuth.getInstance()

        auth.fetchSignInMethodsForEmail(email.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods != null && signInMethods.isNotEmpty()) {
                        custompgbar.dismiss()
                       // Toast.makeText(this,"This Email Address already exists",Toast.LENGTH_SHORT).show()
                        // Email address already exists in Firebase Authentication
                        // Handle the case here, for example show an error message to the user
                    } else {
                        // Email address doesn't exist in Firebase Authentication
                        // Throw an exception with a custom error message
                        Toast.makeText(this,"User created successfully",Toast.LENGTH_SHORT).show()
                        custompgbar.dismiss()
                    }
                }
            }
            .addOnFailureListener {
                custompgbar.dismiss()
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

    }


}