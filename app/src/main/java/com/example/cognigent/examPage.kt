package com.example.cognigent

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

// Define a data class to hold question data
data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int // Index of the correct answer in the 'options' list (0-based)
)

class examPage : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var optionA: RadioButton
    private lateinit var optionB: RadioButton
    private lateinit var optionC: RadioButton
    private lateinit var optionD: RadioButton
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    // List to hold our Question objects
    private val examQuestions = mutableListOf<Question>()
    private var currentQuestionIndex = 0

    // A list to store the user's selected answer for each question (0-based index of option)
    // Initialize with -1 to indicate no answer selected
    private val userAnswers = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_page)

        // Initialize UI components
        questionTextView = findViewById(R.id.questionTextView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        nextButton = findViewById(R.id.nextButton)
        previousButton = findViewById(R.id.previousButton)

        // Populate your exam questions with options and correct answers
        // Make sure to add 4 options for each question
        examQuestions.add(
            Question(
                "Question 1: What is the capital of France?",
                listOf("Berlin", "Madrid", "Paris", "Rome"),
                2 // Paris is at index 2
            )
        )
        examQuestions.add(
            Question(
                "Question 2: What is 2 + 2?",
                listOf("3", "4", "5", "6"),
                1 // 4 is at index 1
            )
        )
        examQuestions.add(
            Question(
                "Question 3: Who painted the Mona Lisa?",
                listOf("Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Claude Monet"),
                2 // Leonardo da Vinci is at index 2
            )
        )
        examQuestions.add(
            Question(
                "Question 4: What is the chemical symbol for water?",
                listOf("O2", "CO2", "H2O", "NACL"),
                2 // H2O is at index 2
            )
        )
        examQuestions.add(
            Question(
                "Question 5: Which planet is known as the Red Planet?",
                listOf("Earth", "Mars", "Jupiter", "Venus"),
                1 // Mars is at index 1
            )
        )

        // Initialize userAnswers list with -1 for each question
        for (i in 0 until examQuestions.size) {
            userAnswers.add(-1) // -1 indicates no answer selected for this question
        }

        // Display the first question and its options
        displayQuestion()

        // Listener for RadioGroup to record user's selection
        optionsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedOptionIndex = when (checkedId) {
                R.id.optionA -> 0
                R.id.optionB -> 1
                R.id.optionC -> 2
                R.id.optionD -> 3
                else -> -1 // No option selected or invalid ID
            }
            if (selectedOptionIndex != -1) {
                userAnswers[currentQuestionIndex] = selectedOptionIndex
            }
            // You can add logic here to give immediate feedback if desired
        }


        // Set OnClickListener for the Next button
        nextButton.setOnClickListener {
            if (currentQuestionIndex < examQuestions.size - 1) {
                currentQuestionIndex++
                displayQuestion()
            }
        }

        // Set OnClickListener for the Previous button
        previousButton.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                displayQuestion()
            }
        }
    }

    /**
     * Updates the TextView with the current question and manages button states.
     */
    private fun displayQuestion() {
        // Get the current question object
        val currentQuestion = examQuestions[currentQuestionIndex]

        // Set the question text
        questionTextView.text = currentQuestion.questionText

        // Set the text for each radio button
        optionA.text = currentQuestion.options[0]
        optionB.text = currentQuestion.options[1]
        optionC.text = currentQuestion.options[2]
        optionD.text = currentQuestion.options[3]

        // Clear any previously selected radio button
        optionsRadioGroup.clearCheck()

        // If the user has previously answered this question, check the corresponding radio button
        val savedAnswerIndex = userAnswers[currentQuestionIndex]
        if (savedAnswerIndex != -1) {
            when (savedAnswerIndex) {
                0 -> optionA.isChecked = true
                1 -> optionB.isChecked = true
                2 -> optionC.isChecked = true
                3 -> optionD.isChecked = true
            }
        }

        // Update the state of navigation buttons
        updateButtonStates()
    }

    /**
     * Enables/disables the Next and Previous buttons based on the current question index.
     */
    private fun updateButtonStates() {
        // Disable previous button if on the first question
        previousButton.isEnabled = currentQuestionIndex > 0
        // Disable next button if on the last question
        nextButton.isEnabled = currentQuestionIndex < examQuestions.size - 1
    }
}