package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    private lateinit var submitButton: Button
    private lateinit var changeLangButton: Button

    private var currentIndex = 0
    private var attempted = 0
    private var solutionMode = false

    private lateinit var timer: CountDownTimer
    val helper = QuestionDatabase(this)
    private val questions = helper.sampleQuestions
    private var timeInMillis = questions.size * 2 * 60 * 1000L
    private val selectedAnswers = IntArray(questions.size) { -1 }
    private val atdArray = IntArray(questions.size) { 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qapage)

        // Initialize views
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
        timerTextView = findViewById(R.id.timer)
        submitButton = findViewById(R.id.submitButton)
        changeLangButton = findViewById(R.id.changeLangButton)

        // Button handlers
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
            saveAnswer()
            val intent = Intent(this, preview::class.java)
            intent.putExtra("arr", atdArray)
            startActivityForResult(intent, 1)
        }

        submitButton.setOnClickListener {
            saveAnswer()
            timer.cancel()
            val score = calculateScore()
            showResultPopup(score, questions.size)
        }

        findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
            showClosePopup()
        }

        solutionMode = intent.getBooleanExtra("solutionMode", false)
        if (!solutionMode) startTimer()

        loadQuestion()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val min = millisUntilFinished / 60000
                val sec = (millisUntilFinished % 60000) / 1000
                timerTextView.text = "⏱ %02d:%02d".format(min, sec)
            }

            override fun onFinish() {
                Toast.makeText(this@QAPage, "Time up!", Toast.LENGTH_SHORT).show()
                showResultPopup(calculateScore(), questions.size)
            }
        }.start()
    }

    private fun loadQuestion() {
        val q = questions[currentIndex]
        testNo.text = "Test No: 1 | Q${currentIndex + 1}/${questions.size}"
        questionText.text = q.text

        questionImage.isVisible = q.type == 2
        q.imageResId?.let { questionImage.setImageResource(it) }

        matchingLayout.isVisible = q.type == 3
        if (q.type == 3) {
            findViewById<TextView>(R.id.matchItemA1).text = q.matchA?.get(0)
            findViewById<TextView>(R.id.matchItemA2).text = q.matchA?.get(1)
            findViewById<TextView>(R.id.matchItemA3).text = q.matchA?.get(2)
            findViewById<TextView>(R.id.matchItemB1).text = q.matchB?.get(0)
            findViewById<TextView>(R.id.matchItemB2).text = q.matchB?.get(1)
            findViewById<TextView>(R.id.matchItemB3).text = q.matchB?.get(2)
        }

        val options = q.options
        option1.text = options[0]
        option2.text = options[1]
        option3.text = options[2]
        option4.text = options[3]

        optionsGroup.clearCheck()
        when (selectedAnswers[currentIndex]) {
            0 -> option1.isChecked = true
            1 -> option2.isChecked = true
            2 -> option3.isChecked = true
            3 -> option4.isChecked = true
        }

        correctAnswerText.isVisible = false
        descriptionText.isVisible = false

        if (solutionMode) {
            option1.isEnabled = false
            option2.isEnabled = false
            option3.isEnabled = false
            option4.isEnabled = false
            submitButton.isEnabled = false
            changeLangButton.isEnabled = false
            showAnswer()
        }

        attempted = selectedAnswers.count { it != -1 }
        attemptedCount.text = "Attempted: $attempted/${questions.size}"
    }

    private fun calculateScore(): Int {
        var score = 0
        for (i in questions.indices) {
            if (selectedAnswers[i] == questions[i].correctIndex) {
                score++
            }
        }
        return score
    }

    private fun saveAnswer() {
        if (option1.isChecked || option2.isChecked || option3.isChecked || option4.isChecked) {
            atdArray[currentIndex] = 1
        }
        val selectedId = optionsGroup.checkedRadioButtonId
        val answer = when (selectedId) {
            R.id.option1 -> 0
            R.id.option2 -> 1
            R.id.option3 -> 2
            R.id.option4 -> 3
            else -> -1
        }
        selectedAnswers[currentIndex] = answer
    }

    private fun showAnswer() {
        val q = questions[currentIndex]
        correctAnswerText.text = "✅ Correct Answer: ${q.options[q.correctIndex]}"
        descriptionText.text = "📘 Explanation: ${q.explanation}"
        correctAnswerText.visibility = View.VISIBLE
        descriptionText.visibility = View.VISIBLE
    }

    private fun showClosePopup() {
        val dialogView = layoutInflater.inflate(R.layout.close_popup, null)
        val popup = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.yesButton).setOnClickListener {
            popup.dismiss()
            startActivity(Intent(this, homepage::class.java))
            finish()
        }

        dialogView.findViewById<Button>(R.id.noButton).setOnClickListener {
            popup.dismiss()
        }

        popup.show()
    }

    private fun showResultPopup(score: Int, total: Int) {
        val dialogView = layoutInflater.inflate(R.layout.result_popup, null)
        val tvScore = dialogView.findViewById<TextView>(R.id.score)
        val btnSeeSolutions = dialogView.findViewById<Button>(R.id.solution)
        val btnGoHome = dialogView.findViewById<Button>(R.id.go_home)

        tvScore.text = "🎯 You scored $score out of $total"

        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.popup_enter)
        dialogView.startAnimation(scaleAnimation)

        val popup = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnSeeSolutions.setOnClickListener {
            popup.dismiss()
            val intent = Intent(this, QAPage::class.java)
            intent.putExtra("solutionMode", true)
            startActivity(intent)
            finish()
        }

        btnGoHome.setOnClickListener {
            popup.dismiss()
            startActivity(Intent(this, homepage::class.java))
            finish()
        }

        popup.window?.setBackgroundDrawableResource(android.R.color.transparent)
        popup.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val selectedIndex = data?.getIntExtra("selected_question_index", -1) ?: -1
            if (selectedIndex in questions.indices) {
                currentIndex = selectedIndex
                loadQuestion()
            }
        }
    }
}
