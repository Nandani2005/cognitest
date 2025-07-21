package com.example.cognigent

data class QuestionModel(
    var type: Int,
    var text: String,
    var imageResId: Int?,
    var options: List<String>,
    var correctIndex: Int,
    var explanation: String,
    var matchA: List<String>? = null,
    var matchB: List<String>? = null,
    var SelectedIndex: Int = -1,
    var Attemp: Int = 0
)