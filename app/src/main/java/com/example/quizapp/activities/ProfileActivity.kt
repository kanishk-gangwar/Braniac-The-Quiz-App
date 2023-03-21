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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.System.err
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.ktx.firestore
import kotlinx.android.synthetic.main.edit_text_set.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProfileActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    private lateinit var dbref : DatabaseReference
    var numberOfEntries: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_profile)

        getQuestions() // to add number of questions needed

        saveQuestions() // save questions to database


        Save.setOnClickListener {

          //  CreateQuiz()
            saveQuizToRealtimeDatabase(numberOfEntries)

        }
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
            numberOfEntries = numberOfEntriesField.text.toString().toIntOrNull()
                ?: 0 // Convert input to integer, default to 0 if not a valid number
            container.removeAllViews() // Clear any existing views in the container
            for (i in 1..numberOfEntries) {
                val view = layoutInflater.inflate(layout.edit_text_set, null)
                container.addView(view)
                val questionNumberTextView = view.findViewById<TextView>(id.questionNumberTextView)
                questionNumberTextView.text = "Question $i"


            }

        }

    }


    private fun CreateQuiz() {


            val db = Firebase.firestore
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val title = dateFormat.format(Date())

            val questions = hashMapOf<String, Any>()
            for (i in 0 until numberOfEntries) {
                val view = editTextContainer.getChildAt(i)

                val description = view.findViewById<EditText>(id.description).text.toString()
                val option1 = view.findViewById<EditText>(id.option1).text.toString()
                val option2 = view.findViewById<EditText>(id.option2).text.toString()
                val option3 = view.findViewById<EditText>(id.option3).text.toString()
                val option4 = view.findViewById<EditText>(id.option4).text.toString()
                val answer = view.findViewById<EditText>(id.correctAnswer).text.toString()




                if (description.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
                    // Show error message and return without saving
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return
                }

                val question = hashMapOf(
                    "description" to description,
                    "option1" to option1,
                    "option2" to option2,
                    "option3" to option3,
                    "option4" to option4,
                    "answer" to answer
                )
                questions["question${i+1}"] = question
            }

            val data = hashMapOf(
                "title" to title,
                "questions" to questions
            )
            db.collection(selectedCategory).document().set(data)

        Toast.makeText(this,"Quiz Saved successfully",Toast.LENGTH_SHORT).show()


        // Clear the EditText fields
        for (i in 0 until numberOfEntries) {
            val view = editTextContainer.getChildAt(i)
            view.findViewById<EditText>(id.description).text.clear()
            view.findViewById<EditText>(id.option1).text.clear()
            view.findViewById<EditText>(id.option2).text.clear()
            view.findViewById<EditText>(id.option3).text.clear()
            view.findViewById<EditText>(id.option4).text.clear()
            view.findViewById<EditText>(id.correctAnswer).text.clear()
        }




    }

    private fun saveQuizToRealtimeDatabase(numberOfQuestions: Int) {
        val database = Firebase.database
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val title = dateFormat.format(Date())

        val questions = mutableMapOf<String, Any>()
        for (i in 1..numberOfQuestions) {
            val view = editTextContainer.getChildAt(i - 1)

            val description = view.findViewById<EditText>(R.id.description).text.toString()
            val option1 = view.findViewById<EditText>(R.id.option1).text.toString()
            val option2 = view.findViewById<EditText>(R.id.option2).text.toString()
            val option3 = view.findViewById<EditText>(R.id.option3).text.toString()
            val option4 = view.findViewById<EditText>(R.id.option4).text.toString()
            val answer = view.findViewById<EditText>(R.id.correctAnswer).text.toString()

            if (description.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
                // Show error message and return without saving
                Toast.makeText(
                    this,
                    "Please fill in all fields for question $i",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            val question = hashMapOf(
                "description" to description,
                "option1" to option1,
                "option2" to option2,
                "option3" to option3,
                "option4" to option4,
                "answer" to answer,
                "Category" to selectedCategory
            )
            questions["question$i"] = question
        }

        val quiz = hashMapOf(
            "category" to selectedCategory,
            "questions" to questions
        )

        val databaseRef = database.getReference("Quizzes").child(selectedCategory)
        databaseRef.push().setValue(questions)

        Toast.makeText(this, "Quiz gone for Review", Toast.LENGTH_SHORT).show()

    }






}



