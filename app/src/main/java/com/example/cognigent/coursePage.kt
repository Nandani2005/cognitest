package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

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


        updateWelcomeMessage(email, welcomeText)


        setCourseClickListener(bcaCard, "BCA", email)
        setCourseClickListener(mcaCard, "MCA", email)
        setCourseClickListener(bbaCard, "BBA", email)
        setCourseClickListener(mbaCard, "MBA", email)
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
