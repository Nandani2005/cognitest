package com.example.cognigent

data class QuestionModel(
    val id: Int,
    val examType: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int,
    var Attemp: Int = -1
)