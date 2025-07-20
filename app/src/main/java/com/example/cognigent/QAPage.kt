package com.example.cognigent

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class QAPage : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var questionImage: ImageView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var option1: RadioButton
    private lateinit var option2: RadioButton
    private lateinit var option3: RadioButton
    private lateinit var option4: RadioButton
    private lateinit var matchingLayout: LinearLayout
    private lateinit var correctAnswerText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var attemptedCount: TextView
    private lateinit var testNo: TextView
    private lateinit var timerTextView: TextView

    private var currentIndex = 0
    private var attempted = 0

    private lateinit var timer: CountDownTimer
    private var timeInMillis = 15 * 60 * 1000L // 15 minutes

    data class Question(
        val type: Int,
        val text: String,
        val imageResId: Int?,
        val options: List<String>,
        val correctAnswerIndex: Int,
        val explanation: String,
        val matchA: List<String>? = null,
        val matchB: List<String>? = null
    )

    private val questions = listOf(
        Question(
            type = 1,
            text = "What is 2 + 2?",
            imageResId = null,
            options = listOf("3", "4", "5", "6"),
            correctAnswerIndex = 1,
            explanation = "2 + 2 = 4"
        ),
        Question(
            type = 2,
            text = "Identify the fruit",
            imageResId = R.drawable.ic_launcher_background,
            options = listOf("Apple", "Banana", "Mango", "Orange"),
            correctAnswerIndex = 2,
            explanation = "It's a mango image."
        ),
        Question(
            type = 3,
            text = "Match the languages with type",
            imageResId = null,
            options = listOf("A-1, B-2, C-3", "A-2, B-1, C-3", "A-3, B-2, C-1", "A-1, B-3, C-2"),
            correctAnswerIndex = 0,
            explanation = "Java is OOP, Python is scripting, C++ is low level",
            matchA = listOf("A. Java", "B. Python", "C. C++"),
            matchB = listOf("1. Object Oriented", "2. Scripting", "3. Low Level")
        )
    )

    private val selectedAnswers = IntArray(questions.size) { -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qapage)

        questionText = findViewById(R.id.questionText)
        questionImage = findViewById(R.id.questionImage)
        optionsGroup = findViewById(R.id.optionsGroup)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        matchingLayout = findViewById(R.id.matchingLayout)
        correctAnswerText = findViewById(R.id.correctAnswerText)
        descriptionText = findViewById(R.id.descriptionText)
        attemptedCount = findViewById(R.id.attemptedCount)
        testNo = findViewById(R.id.testno)
        timerTextView = TextView(this)
        timerTextView.textSize = 16f

        val layout = findViewById<LinearLayout>(R.id.mainContainer)
        layout.addView(timerTextView, 1) // Add below test no

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            saveAnswer()
            if (currentIndex < questions.size - 1) {
                currentIndex++
                loadQuestion()
            }
        }

        findViewById<Button>(R.id.btnPrevious).setOnClickListener {
            saveAnswer()
            if (currentIndex > 0) {
                currentIndex--
                loadQuestion()
            }
        }

        findViewById<Button>(R.id.btnPreview).setOnClickListener {
            showAnswer()
        }

        findViewById<Button>(R.id.submitButton).setOnClickListener {
            timer.cancel()
            Toast.makeText(this, "Test submitted!", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
            finish()
        }

        startTimer()
        loadQuestion()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val min = millisUntilFinished / 60000
                val sec = (millisUntilFinished % 60000) / 1000
                timerTextView.text = "Time Left: %02d:%02d".format(min, sec)
            }

            override fun onFinish() {
                Toast.makeText(this@QAPage, "Time up!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }

    private fun loadQuestion() {
        val q = questions[currentIndex]

        // Update Question and Type
        questionText.text = q.text
        testNo.text = "Test No: 1 | Q${currentIndex + 1}/${questions.size}"

        // Show/hide image
        questionImage.isVisible = q.type == 2
        q.imageResId?.let { questionImage.setImageResource(it) }

        // Show/hide matching
        matchingLayout.isVisible = q.type == 3
        if (q.type == 3) {
            findViewById<TextView>(R.id.matchItemA1).text = q.matchA?.get(0)
            findViewById<TextView>(R.id.matchItemA2).text = q.matchA?.get(1)
            findViewById<TextView>(R.id.matchItemA3).text = q.matchA?.get(2)
            findViewById<TextView>(R.id.matchItemB1).text = q.matchB?.get(0)
            findViewById<TextView>(R.id.matchItemB2).text = q.matchB?.get(1)
            findViewById<TextView>(R.id.matchItemB3).text = q.matchB?.get(2)
        }

        // Update options
        val options = q.options
        option1.text = options[0]
        option2.text = options[1]
        option3.text = options[2]
        option4.text = options[3]

        // Set selected answer if previously attempted
        optionsGroup.clearCheck()
        when (selectedAnswers[currentIndex]) {
            0 -> option1.isChecked = true
            1 -> option2.isChecked = true
            2 -> option3.isChecked = true
            3 -> option4.isChecked = true
        }

        // Hide correct answer/description
        correctAnswerText.isVisible = false
        descriptionText.isVisible = false

        attempted = selectedAnswers.count { it != -1 }
        attemptedCount.text = "Attempted: $attempted/${questions.size}"
    }

    private fun saveAnswer() {
        val selectedId = optionsGroup.checkedRadioButtonId
        val answer = when (selectedId) {
            R.id.option1 -> 0
            R.id.option2 -> 1
            R.id.option3 -> 2
            R.id.option4 -> 3
            else -> -1
        }
        if (answer != -1) {
            selectedAnswers[currentIndex] = answer
        }
    }

    private fun showAnswer() {
        val q = questions[currentIndex]
        correctAnswerText.text = "Correct Answer: ${q.options[q.correctAnswerIndex]}"
        descriptionText.text = "Explanation: ${q.explanation}"
        correctAnswerText.isVisible = true
        descriptionText.isVisible = true
    }
}
