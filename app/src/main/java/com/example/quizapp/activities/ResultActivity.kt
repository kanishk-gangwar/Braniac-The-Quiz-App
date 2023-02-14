package com.example.quizapp.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.example.quizapp.R
import com.example.quizapp.activities.models.Quiz
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    lateinit var quiz : Quiz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        setUpViews()
    }

    private fun setUpViews() {
        val quizdata = intent.getStringExtra("QUIZ")
        quiz = Gson().fromJson<Quiz>(quizdata,Quiz::class.java)
        setAnswerView()
        calculateScore()
    }

    private fun setAnswerView() {
        val builder = StringBuilder("")
        for (entry in quiz.questions.entries) {
            val question = entry.value
            builder.append("<font color'#18206F'><b>Question: ${question.description}</b></font><br/><br/>")
            builder.append("<font color='#009688'>Answer: ${question.answer}</font><br/><br/>")
        }
        txtAnswer.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT);
    }

    private fun calculateScore() {
        var correctAnswer : Int = 1

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