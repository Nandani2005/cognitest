package com.example.cognigent

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddQuestionActivity : AppCompatActivity() {

    private lateinit var inputExamType: EditText
    private lateinit var inputQuestion: EditText
    private lateinit var inputOpt1: EditText
    private lateinit var inputOpt2: EditText
    private lateinit var inputOpt3: EditText
    private lateinit var inputOpt4: EditText
    private lateinit var inputCorrectAnswer: EditText
    private lateinit var btnInsert: Button
    private lateinit var deleteAll: Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.insert_que)

        dbHelper = DatabaseHelper(this)

        inputExamType = findViewById(R.id.inputExamType)
        inputQuestion = findViewById(R.id.inputQuestion)
        inputOpt1 = findViewById(R.id.inputOpt1)
        inputOpt2 = findViewById(R.id.inputOpt2)
        inputOpt3 = findViewById(R.id.inputOpt3)
        inputOpt4 = findViewById(R.id.inputOpt4)
        inputCorrectAnswer = findViewById(R.id.inputCorrectAnswer)
        btnInsert = findViewById(R.id.btnInsert)
        deleteAll = findViewById(R.id.deleteAll)

        btnInsert.setOnClickListener {
            val examType = inputExamType.text.toString().trim()
            val question = inputQuestion.text.toString().trim()
            val opt1 = inputOpt1.text.toString().trim()
            val opt2 = inputOpt2.text.toString().trim()
            val opt3 = inputOpt3.text.toString().trim()
            val opt4 = inputOpt4.text.toString().trim()
            val correctAns = inputCorrectAnswer.text.toString().trim()

            if (examType.isNotEmpty() && question.isNotEmpty() &&
                opt1.isNotEmpty() && opt2.isNotEmpty() &&
                opt3.isNotEmpty() && opt4.isNotEmpty() && correctAns.isNotEmpty()
            ) {
                val result = dbHelper.insertQuestion(
                    examType, question, opt1, opt2, opt3, opt4, correctAns
                )

                if (result != -1L) {
                    Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        deleteAll.setOnClickListener {
            val dbHelper = DatabaseHelper(this)
            val deletedRows = dbHelper.deleteAllQuestions()

            if (deletedRows > 0) {
                Toast.makeText(this, "All questions deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No questions to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        inputExamType.text.clear()
        inputQuestion.text.clear()
        inputOpt1.text.clear()
        inputOpt2.text.clear()
        inputOpt3.text.clear()
        inputOpt4.text.clear()
        inputCorrectAnswer.text.clear()
    }
}
