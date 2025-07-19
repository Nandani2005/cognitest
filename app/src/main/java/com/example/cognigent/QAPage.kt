package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QAPage : AppCompatActivity() {

    private lateinit var dbHelper: QuestionDatabase
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
    private lateinit var closeBtn: Button
    private lateinit var testNumber: TextView
    private lateinit var questionNumber: TextView
    private lateinit var timerText: TextView

    private var totalTimeInMillis: Long = 30 * 60 * 1000 // 30 minutes
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qapage)

        dbHelper = QuestionDatabase(this)
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
        closeBtn = findViewById(R.id.closeButton)
        testNumber = findViewById(R.id.testNumber)
        questionNumber = findViewById(R.id.questionNumber)
        timerText = findViewById(R.id.timerText)

        if (questionList.isNotEmpty()) {
            showQuestion(currentIndex)
            startTimer()
        } else {
            Toast.makeText(this, "No questions found in database.", Toast.LENGTH_LONG).show()
        }

        optionsGroup.setOnCheckedChangeListener { _, checkedId ->
            val selected = when (checkedId) {
                R.id.optionA -> 0
                R.id.optionB -> 1
                R.id.optionC -> 2
                R.id.optionD -> 3
                else -> -1
            }
            questionList[currentIndex].Attemp = selected
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
            val intent = Intent(this, preview::class.java)
//            intent.putParcelableArrayListExtra("attemptedQuestions", ArrayList(questionList))
            startActivity(intent)
        }


        closeBtn.setOnClickListener {
            countDownTimer.cancel()
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

        when (question.Attemp) {
            0 -> optionA.isChecked = true
            1 -> optionB.isChecked = true
            2 -> optionC.isChecked = true
            3 -> optionD.isChecked = true
        }
    }


    private fun calculateScore(): Int {
        return questionList.count { it.Attemp == it.correctIndex }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                timerText.text = String.format("Time Left: %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                val score = calculateScore()
                dbHelper.saveResult(score)
                timerText.text = "Time's up!"
                disableOptions()

                AlertDialog.Builder(this@QAPage)
                    .setTitle("Time's Up!")
                    .setMessage("Your Score: $score / ${questionList.size}")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .setCancelable(false)
                    .show()
            }
        }
        countDownTimer.start()
    }

    private fun disableOptions() {
        for (i in 0 until optionsGroup.childCount) {
            optionsGroup.getChildAt(i).isEnabled = false
        }
        prevBtn.isEnabled = false
        nextBtn.isEnabled = false
        previewBtn.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}
