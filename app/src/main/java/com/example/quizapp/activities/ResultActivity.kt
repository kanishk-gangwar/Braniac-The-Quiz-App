package com.example.quizapp.activities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.example.quizapp.R
import com.example.quizapp.activities.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.edit_text_set.*

class ResultActivity : AppCompatActivity() {
    lateinit var quiz : Quiz
    lateinit var firebaseAuth: FirebaseAuth
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        firebaseAuth = FirebaseAuth.getInstance()

        setUpViews()
    }

    private fun setUpViews() {
        val quizdata = intent.getStringExtra("QUIZ")
        quiz = Gson().fromJson<Quiz>(quizdata,Quiz::class.java)
        setAnswerView()

        val numQuestions = quiz.questions.size
        var numCorrectAnswers = 0

        for (question in quiz.questions.values) {
            if (question.answer == question.userAnswer) {
                numCorrectAnswers++
            }
        }

        val score = numCorrectAnswers * 10
        val user = firebaseAuth.currentUser!!.email.toString()
        db.collection("User").document(user).update("score",score)
            .addOnSuccessListener {
                Log.d(TAG,"Updated")
            }
            .addOnFailureListener { e ->
                Log.w(TAG,"Error",e)
            }
        txtScore.text = "Your Score: $score / ${numQuestions * 10}"
    }

    private fun setAnswerView() {
        val builder = StringBuilder("")
        for (entry in quiz.questions.entries) {
            val question = entry.value
            builder.append("<font color'#18206F'><b>Question: ${question.description}</b></font><br/><br/>")
            builder.append("<font color='#009688'>Answer: ${question.answer}</font><br/>")
            if (question.answer == question.userAnswer) {
                builder.append("<font color='#4CAF50'>Your Answer: ${question.userAnswer}</font><br/><br/>")
            } else {
                builder.append("<font color='#F44336'>Your Answer: ${question.userAnswer}</font><br/><br/>")
            }
        }
        txtAnswer.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
    }

    private fun calculateScore() {
        var correctAnswer = 0

        quiz.questions.entries.forEach { entry ->
            val question = entry.value
            if (question.answer == question.userAnswer) {
                correctAnswer++
            }

            Log.d("abhay", "$correctAnswer")
        }

        var score : Int = correctAnswer * 10
        txtScore.text = "Your Score : $score"

    }



}