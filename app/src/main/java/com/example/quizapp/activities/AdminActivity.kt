package com.example.quizapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.activities.models.Questions
import com.example.quizapp.activities.models.review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.Category

class AdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var savebtn : Button
    private lateinit var Category : String
    private lateinit var SubCategory : String
    private lateinit var key : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.reviewrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        key = intent.getStringExtra("key")!!
        Category = intent.getStringExtra("Category")!!

        // showQuestions(searchtext.text.toString())
        showQuestions(Category,key)

    }


    private fun showQuestions(subject: String, quizKey: String) {
        val database = Firebase.database
        val databaseRef = database.getReference("Quizzes").child(subject).child(quizKey)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val questions = mutableListOf<Questions>()

                for (questionSnapshot in dataSnapshot.children) {
                    val questionData = questionSnapshot.value as Map<*, *>
                    val question = Questions(
                        questionData["description"].toString(),
                        questionData["option1"].toString(),
                        questionData["option2"].toString(),
                        questionData["option3"].toString(),
                        questionData["option4"].toString(),
                        questionData["answer"].toString(),
                        questionData["Category"].toString()
                    )
                    SubCategory = questionData["Category"].toString()
                    questions.add(question)
                    Log.d("sb",SubCategory)
                }

                val quiz = review(
                    quizKey,
                    dataSnapshot.child("Category").value.toString(),
                    questions
                )


                savebtn = findViewById(R.id.Savebtn)
                savebtn.isVisible = true
                savebtn.setOnClickListener {
                    saveQuizToFirestore(quiz)
                    val intent = Intent(this@AdminActivity,KeyReviewActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                // pass the quiz object to a function that displays the quiz details
                recyclerView.adapter = reviewAdapter(questions)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadQuestions:onCancelled", error.toException())
            }
        })
    }


    private fun saveQuizToFirestore(quiz: review) {
        val db = Firebase.firestore
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val title = dateFormat.format(Date())

        val questions = hashMapOf<String, Any>()
        for ((index, question) in quiz.questions.withIndex()) {
            val questionData = hashMapOf(
                "description" to question.description,
                "option1" to question.option1,
                "option2" to question.option2,
                "option3" to question.option3,
                "option4" to question.option4,
                "answer" to question.answer
            )
            questions["question${index + 1}"] = questionData

        }

        val data = hashMapOf(
            "title" to title,
            "questions" to questions
        )
        Log.d("Category",quiz.category)
        db.collection(SubCategory).document().set(data)


        val databaseRef = FirebaseDatabase.getInstance().getReference("Quizzes").child(Category)
        databaseRef.child(key).removeValue().addOnSuccessListener {
            Toast.makeText(this,"Quiz Saved successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong!!",Toast.LENGTH_SHORT).show()
        }

    }


}