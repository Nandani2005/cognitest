package com.example.cognigent

import QuestionModel
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QAPage : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var questionList: List<QuestionModel>
    private var currentIndex = 0

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var optionA: RadioButton
    private lateinit var optionB: RadioButton
    private lateinit var optionC: RadioButton
    private lateinit var optionD: RadioButton
    private lateinit var prevBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var previewBtn: Button
    private lateinit var testNumber: TextView
    private lateinit var questionNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qapage)

        dbHelper = DatabaseHelper(this)
       // questionList = dbHelper.getAllQuestions()


        questionList = dbHelper.getQuestionsByExamType("mba")



        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        prevBtn = findViewById(R.id.prevButton)
        nextBtn = findViewById(R.id.nextButton)
        previewBtn = findViewById(R.id.previewButton)
        testNumber = findViewById(R.id.testNumber)
        questionNumber = findViewById(R.id.questionNumber)

        if (questionList.isNotEmpty()) {
            showQuestion(currentIndex)
        } else {
            Toast.makeText(this, "No questions available.", Toast.LENGTH_SHORT).show()
        }

        prevBtn.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showQuestion(currentIndex)
            }
        }

        nextBtn.setOnClickListener {
            if (currentIndex < questionList.size - 1) {
                currentIndex++
                showQuestion(currentIndex)
            }
        }

        previewBtn.setOnClickListener {
            Toast.makeText(this, "Preview feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.closeButton).setOnClickListener {
            finish()
        }
    }

    private fun showQuestion(index: Int) {
        val question = questionList[index]
        questionText.text = question.questionText
        optionA.text = question.optionA
        optionB.text = question.optionB
        optionC.text = question.optionC
        optionD.text = question.optionD

        questionNumber.text = "Question ${index + 1} of ${questionList.size}"
        testNumber.text = "Test 1"

        optionsGroup.clearCheck()

        // Restore selection if user already answered this
        when (question.selectedIndex) {
            0 -> optionA.isChecked = true
            1 -> optionB.isChecked = true
            2 -> optionC.isChecked = true
            3 -> optionD.isChecked = true
        }
    }

}
