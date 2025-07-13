package com.example.cognigent

data class Question(
    val id: Int,
    val examType: String,
    val ques: String,
    val opt1: String,
    val opt2: String,
    val opt3: String,
    val opt4: String,
    val correctAns: String
)
