package com.example.quizapp.activities.models


//class Quiz{
//    var id : String? = null
//    var title : String? = null
//    var questions : MutableMap<String,Questions> = mutableMapOf()
//
//    constructor(id:String?,title:String?,questions:MutableMap<String,Questions>){
//        this.id = id
//        this.title = title
//        this.questions = questions
//    }
//}
data class Quiz(
    var id : String = "",
    var title : String = "",
    var questions : MutableMap<String,Questions> = mutableMapOf()
)