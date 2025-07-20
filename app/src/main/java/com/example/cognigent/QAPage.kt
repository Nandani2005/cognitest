package com.example.cognigent

import android.app.Activity
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
        questionList = dbHelper.getQuestionsByExamType("mba")


        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        optionA = findViewById(R.id.option1)
        optionB = findViewById(R.id.option2)
        optionC = findViewById(R.id.option3)
        optionD = findViewById(R.id.option4)
        prevBtn = findViewById(R.id.btnPrevious)
        nextBtn = findViewById(R.id.btnNext)
        previewBtn = findViewById(R.id.btnPreview)
        closeBtn = findViewById(R.id.closeButton)
        testNumber = findViewById(R.id.testno)
        val attempted = findViewById<TextView>(R.id.attemptedCount)
        timerText = findViewById(R.id.timer)

        if (questionList.isNotEmpty()) {
            showQuestion(currentIndex)
            startTimer()
        } else {
            Toast.makeText(this, "No questions found in database.", Toast.LENGTH_LONG).show()
        }
        attempted.text = "Attempted: ${questionList.count { it.Attemp != -1 }}"
        prevBtn.isEnabled = currentIndex > 0
        nextBtn.isEnabled = currentIndex < questionList.size - 1
        optionsGroup.setOnCheckedChangeListener { _, checkedId ->
            val selected = when (checkedId) {
                R.id.option1 -> 0
                R.id.option2 -> 1
                R.id.option3 -> 2
                R.id.option4 -> 3
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
            startActivityForResult(intent, 1001)
        }



        closeBtn.setOnClickListener {
            showLogoutPopup()
            countDownTimer.cancel()
            val score = calculateScore()
            dbHelper.saveResult(score,"MBA")
            Toast.makeText(this, "Score: $score / ${questionList.size}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val jumpTo = data?.getIntExtra("jumpToQuestion", -1) ?: -1
            if (jumpTo in questionList.indices) {
                currentIndex = jumpTo
                showQuestion(currentIndex)
            }
        }
    }

    private fun showLogoutPopup() {
        val inflater = layoutInflater
        val popupView = inflater.inflate(R.layout.logout_popup, null)
        val confirmBtn = popupView.findViewById<Button>(R.id.yesButton)
        val cancelBtn = popupView.findViewById<Button>(R.id.noButton)
        confirmBtn.setOnClickListener {
            val intent = Intent(this, loginpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        cancelBtn.setOnClickListener {

        }
        val popup = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

// IMPORTANT: Show the popup
        popup.showAtLocation(popupView, android.view.Gravity.CENTER, 0, 0)
        true
        popup.isFocusable = true
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
                dbHelper.saveResult(score,"BCA")
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
