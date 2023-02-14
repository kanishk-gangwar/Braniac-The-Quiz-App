package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.quizapp.R
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() {
    private lateinit var getstarted: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val auth = FirebaseAuth.getInstance()
        getstarted = findViewById(R.id.IntroBtn)
        if(auth.currentUser != null){
            Toast.makeText(this,"User is already Logged In",Toast.LENGTH_SHORT).show()
            redirect("MAIN")
        }

        getstarted.setOnClickListener {
            redirect("LOGIN")
        }

     //   getstarted.setOnClickListener {

//            val user = FirebaseAuth.getInstance().currentUser
//            if (user != null) {
//                // User is signed in
//                val i = Intent(this@IntroActivity, MainActivity::class.java)
//                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(i)
//                Toast.makeText(this,"User is already Logged In",Toast.LENGTH_SHORT).show()
//            }
     //   }
    }

    private fun redirect(name: String) {
        val intent  = when(name){
            "LOGIN" -> Intent(this,LoginActivity::class.java)
            "MAIN" -> Intent(this,MainActivity::class.java)
            else -> throw Exception("no path exists")
        }
        startActivity(intent)
        finish()

    }
}