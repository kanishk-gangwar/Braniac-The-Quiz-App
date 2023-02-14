package com.example.quizapp.activities.models

data class Questions(
    var description : String = "",
    var option1 : String = "",
    var option2 : String = "",
    var option3 : String = "",
    var option4 : String = "",
    var answer : String = "",
    var userAnswer : String = ""
)