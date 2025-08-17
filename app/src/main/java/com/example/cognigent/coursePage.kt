package com.example.cognigent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper
import org.json.JSONArray

class coursePage : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_page)

        val email = intent.getStringExtra("email") ?: ""
        dbHelper = DBHelper(this)


        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val bcaCard = findViewById<LinearLayout>(R.id.bca)
        val mcaCard = findViewById<LinearLayout>(R.id.mca)
        val bbaCard = findViewById<LinearLayout>(R.id.bba)
        val mbaCard = findViewById<LinearLayout>(R.id.mba)
        val addques = findViewById<Button>(R.id.addques)
        addques.setOnClickListener {
            loadQuestionsFromJson()
        }


        updateWelcomeMessage(email, welcomeText)


        setCourseClickListener(bcaCard, "BCA", email)
        setCourseClickListener(mcaCard, "MCA", email)
        setCourseClickListener(bbaCard, "BBA", email)
        setCourseClickListener(mbaCard, "MBA", email)
    }

    fun loadQuestionsFromJson() {
        val jsonStr = assets.open("questionsheet.json")
            .bufferedReader().use { it.readText() }

        val jsonArray = JSONArray(jsonStr)
        val dbHelper = QuestionDatabase(this)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            Log.d("QUESTATUS", obj.toString())
            dbHelper.insertQuestionFromJSON(
                coursetype = obj.optString("coursetype"),
                queType = obj.optInt("queType"),
                que = obj.optString("que"),
                opt1 = obj.optString("opt1"),
                opt2 = obj.optString("opt2"),
                opt3 = obj.optString("opt3"),
                opt4 = obj.optString("opt4"),
                correctAnswer = obj.optInt("CorrectAnswer"),
                correctAnswerDetails = obj.optString("CorrectAnswerDetails"),
                queImage = obj.optString("queImage"),
                title1 = obj.optString("title1"),
                title2 = obj.optString("title2"),
                name1 = obj.optString("name1"),
                name2 = obj.optString("name2"),
                name3 = obj.optString("name3"),
                name4 = obj.optString("name4"),
                value1 = obj.optString("value1"),
                value2 = obj.optString("value2"),
                value3 = obj.optString("value3"),
                value4 = obj.optString("value4"),
                dropdown = obj.optString("@dropdown")
            )
        }
    }


    private fun updateWelcomeMessage(email: String, welcomeText: TextView) {
        val user = dbHelper.getUserNameAndCourse(email)
        if (user != null && user.first.isNotEmpty()) {
            welcomeText.text = "Welcome, ${user.first}!"
        } else {
            welcomeText.text = "Welcome!"
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setCourseClickListener(card: LinearLayout, course: String, email: String) {
        card.setOnClickListener {
            if (dbHelper.updateCourse(email, course)) {
                navigateToHome(email, course)
            } else {
                Toast.makeText(this, "Failed to save course. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun navigateToHome(email: String, course: String) {
        val intent = Intent(this , homepage::class.java).apply {
            putExtra("email", email)
            putExtra("selectedCourse", course)
        }
        startActivity(intent)
        finish()
    }
}
