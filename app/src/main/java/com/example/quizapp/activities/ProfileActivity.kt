package com.example.quizapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.quizapp.R
import com.example.quizapp.R.*
import com.example.quizapp.activities.models.spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.System.err
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    private lateinit var dbref : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_profile)

        getQuestions() // to add number of questions needed
      //  dateselector()
        saveQuestions() // save questions to database
    }

    private fun dateselector() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

// Format the date as a string
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time)

// Set the string as the text of your EditText
      //  customdate.setText(dateString)

        pushdate()

    }

    private fun pushdate() {


    }
    private var selectedCategory: String = ""

    private fun saveQuestions() {


        dbref = FirebaseDatabase.getInstance().getReference("Category")
        val data =  arrayOf("Select Category","Science","GK","Maths","Social Science","English")

        spinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,data)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = data[position]

                dbref.child(selectedCategory).setValue(selectedCategory)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


    }

    private fun getQuestions() {
        val container: LinearLayout = findViewById(id.editTextContainer)
        val generateButton: Button = findViewById(id.generateButton)

        generateButton.setOnClickListener {
            val numberOfEntriesField: EditText = findViewById(id.numberOfEntries)
            val numberOfEntries = numberOfEntriesField.text.toString().toIntOrNull()
                ?: 0 // Convert input to integer, default to 0 if not a valid number
            container.removeAllViews() // Clear any existing views in the container
            for (i in 1..numberOfEntries) {
                val view = layoutInflater.inflate(layout.edit_text_set, null)
                container.addView(view)

                val questionNumberTextView = view.findViewById<TextView>(id.questionNumberTextView)
                questionNumberTextView.text = "Question $i"


            }
//            val etdate = customdate.text.toString()
//            var date = spinner(etdate)
//            dbref.child(selectedCategory).child(etdate).setValue(date)
//                .addOnCompleteListener {
//                    Toast.makeText(this@ProfileActivity,"Data Inserted Successfully",Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this@ProfileActivity,"Error $err",Toast.LENGTH_SHORT).show()
//                }
//

//            val etdate = customdate.text.toString()
//            val date = spinner(etdate)
//            dbref = FirebaseDatabase.getInstance().getReference("Category")
//            dbref.child(selectedCategory).child(etdate).setValue(etdate)



        }

    }


}