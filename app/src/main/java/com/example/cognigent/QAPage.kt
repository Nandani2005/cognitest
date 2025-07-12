import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cognigent.R

class QAPage : AppCompatActivity() {

    private lateinit var dbHelper: MCQDatabaseHelper
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
    private lateinit var submitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qapage)

        // Bind UI elements
        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        prevBtn = findViewById(R.id.prevButton)
        nextBtn = findViewById(R.id.nextButton)


        dbHelper = MCQDatabaseHelper(this)

        // Insert sample questions if DB is empty
        if (dbHelper.getAllQuestions().isEmpty()) {
            dbHelper.insertQuestion("Capital of India?", "Mumbai", "Delhi", "Chennai", "Kolkata", 1)
            dbHelper.insertQuestion("5 + 3 = ?", "6", "7", "8", "9", 2)
        }

        questionList = dbHelper.getAllQuestions()
        displayQuestion()

        prevBtn.setOnClickListener {
            saveSelectedOption()
            if (currentIndex > 0) {
                currentIndex--
                displayQuestion()
            }
        }

        nextBtn.setOnClickListener {
            saveSelectedOption()
            if (currentIndex < questionList.size - 1) {
                currentIndex++
                displayQuestion()
            }
        }

        submitBtn.setOnClickListener {
            saveSelectedOption()
            val score = questionList.count {
                it.selectedIndex == it.correctIndex
            }
            Toast.makeText(this, "Your Score: $score / ${questionList.size}", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayQuestion() {
        val question = questionList[currentIndex]
        questionText.text = "${currentIndex + 1}. ${question.questionText}"
        optionA.text = question.optionA
        optionB.text = question.optionB
        optionC.text = question.optionC
        optionD.text = question.optionD

        optionsGroup.clearCheck()
        when (question.selectedIndex) {
            0 -> optionA.isChecked = true
            1 -> optionB.isChecked = true
            2 -> optionC.isChecked = true
            3 -> optionD.isChecked = true
        }

        prevBtn.isEnabled = currentIndex > 0
        nextBtn.isEnabled = currentIndex < questionList.size - 1
    }

    private fun saveSelectedOption() {
        val selectedIndex = when (optionsGroup.checkedRadioButtonId) {
            R.id.optionA -> 0
            R.id.optionB -> 1
            R.id.optionC -> 2
            R.id.optionD -> 3
            else -> -1
        }

        if (selectedIndex != -1) {
            val qId = questionList[currentIndex].id
            dbHelper.saveStudentResponse(qId, selectedIndex)
            questionList = dbHelper.getAllQuestions()
        }
    }
}
