package com.example.cognigent// Change to your package name

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cognigent.R

class ReviewActivity : androidx.appcompat.app.AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var rvQuestionStatus: RecyclerView
    private lateinit var tvScore: TextView
    private lateinit var tvPercentage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        databaseHelper = DatabaseHelper(this)
        rvQuestionStatus = findViewById(R.id.rv_question_status)
        tvScore = findViewById(R.id.tv_score)
        tvPercentage = findViewById(R.id.tv_percentage)

        // For demonstration: Add some dummy data if the database is empty
        // In a real app, you would populate this after a quiz attempt.
        if (databaseHelper.getAllQuestions().isEmpty()) {
            addDummyData()
        }

        displayQuizResults()
    }

    private fun addDummyData() {
        // Add some dummy questions
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "What is 2+2?",
                correctAnswer = "4"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Capital of France?",
                correctAnswer = "Paris"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Is Earth flat?",
                correctAnswer = "No"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Color of the sky?",
                correctAnswer = "Blue"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Largest ocean?",
                correctAnswer = "Pacific"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "What is 5*5?",
                correctAnswer = "25"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Sun is a star?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Python is a snake?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Water boils at 100C?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Google invented Android?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Mars is red?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Is 10 even?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Apple makes cars?",
                correctAnswer = "No"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Humans have 2 legs?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "What is 1+1?",
                correctAnswer = "2"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "What is 3*3?",
                correctAnswer = "9"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Is Pluto a planet?",
                correctAnswer = "No"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "India is in Asia?",
                correctAnswer = "Yes"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "What is 10-5?",
                correctAnswer = "5"
            )
        )
        databaseHelper.addQuestion(
            _root_ide_package_.com.example.cognigent.Question(
                questionText = "Birds can fly?",
                correctAnswer = "Yes"
            )
        )


        // Add some dummy attempts
        databaseHelper.addAttempt(Attempt(questionId = 1, userAnswer = "4", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 2, userAnswer = "Paris", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 3, userAnswer = "Yes", isCorrect = false)) // Incorrect
        databaseHelper.addAttempt(Attempt(questionId = 4, userAnswer = "Blue", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 5, userAnswer = "Atlantic", isCorrect = false)) // Incorrect
        databaseHelper.addAttempt(Attempt(questionId = 6, userAnswer = "25", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 7, userAnswer = "Yes", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 8, userAnswer = "No", isCorrect = false)) // Incorrect
        databaseHelper.addAttempt(Attempt(questionId = 9, userAnswer = "Yes", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 10, userAnswer = "Yes", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 11, userAnswer = "Yes", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 12, userAnswer = "Yes", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 13, userAnswer = "Yes", isCorrect = false)) // Incorrect
        databaseHelper.addAttempt(Attempt(questionId = 14, userAnswer = "No", isCorrect = false)) // Incorrect
        databaseHelper.addAttempt(Attempt(questionId = 15, userAnswer = "2", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 16, userAnswer = "9", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 17, userAnswer = "Yes", isCorrect = false)) // Incorrect
        databaseHelper.addAttempt(Attempt(questionId = 18, userAnswer = "Yes", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 19, userAnswer = "5", isCorrect = true))
        databaseHelper.addAttempt(Attempt(questionId = 20, userAnswer = "Yes", isCorrect = true))
    }

    private fun displayQuizResults() {
        val attempts = databaseHelper.getAllAttempts()
        val correctCount = databaseHelper.getCorrectAttemptsCount()
        val totalCount = databaseHelper.getTotalAttemptsCount()

        val percentage = if (totalCount > 0) (correctCount.toFloat() / totalCount * 100).toInt() else 0

        tvScore.text = "$correctCount/$totalCount"
        tvPercentage.text = "You are right\n$percentage%"

        rvQuestionStatus.layoutManager = GridLayoutManager(this, 3) // 3 columns as per image
        val adapter = QuestionStatusAdapter(attempts)
        rvQuestionStatus.adapter = adapter
    }
}