package com.example.cognigent

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class preview : AppCompatActivity() {

    // Example mock data (replace with your actual logic)
    private val totalQuestions = 20
    private val attemptedStatus = BooleanArray(totalQuestions) { it % 2 == 0 } // even-numbered attempted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val questionListLayout = findViewById<LinearLayout>(R.id.questionList)
        val btnGoBack = findViewById<Button>(R.id.btnGoBack)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitTest)

        // Populate question list
        for (i in 1..totalQuestions) {
            val status = if (attemptedStatus[i - 1]) "Attempted" else "Not Attempted"

            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 12, 8, 12)
            }

            val questionNumber = TextView(this).apply {
                text = "Q$i:"
                textSize = 16f
                setTextColor(Color.BLACK)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val statusView = TextView(this).apply {
                text = status
                textSize = 16f
                setTextColor(if (status == "Attempted") Color.parseColor("#2E7D32") else Color.RED)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            row.addView(questionNumber)
            row.addView(statusView)
            questionListLayout.addView(row)
        }

        // Go back to test
        btnGoBack.setOnClickListener {
            finish() // or use: startActivity(Intent(this, QAPage::class.java))
        }

        // Submit test logic
        btnSubmit.setOnClickListener {
            Toast.makeText(this, "Test submitted successfully!", Toast.LENGTH_LONG).show()
            // You can also navigate to result page or finish the test
        }
    }
}
