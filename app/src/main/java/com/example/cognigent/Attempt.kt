package com.example.cognigent// Change to your package name

data class Attempt(
    val id: Int = 0,
    val questionId: Int,
    val userAnswer: String,
    val isCorrect: Boolean
)