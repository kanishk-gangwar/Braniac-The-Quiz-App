package com.example.quizapp.activities.models

data class review (
    val quizKey: String,
    val category: String,
    val questions: List<Questions>
)