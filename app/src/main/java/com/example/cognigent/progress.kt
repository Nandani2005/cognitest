package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class progress : AppCompatActivity() {

    private lateinit var subjectSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress)

        val email = intent.getStringExtra("email") ?: ""
        val course = intent.getStringExtra("selectedCourse") ?: "Select Course"
        val newcourse = intent.getStringExtra("newSubject")

        // Initialize spinner
        subjectSpinner = findViewById(R.id.subjectSpinner)

        // Setup bottom navigation
        findViewById<ImageView>(R.id.nav_progress).setOnClickListener {
            val intent1 = Intent(this, progress::class.java)
            intent1.putExtra("email", email)
            intent1.putExtra("selectedCourse", course)
            startActivity(intent1)
        }

        findViewById<ImageView>(R.id.nav_result).setOnClickListener {
            val intent2 = Intent(this, notification::class.java)
            intent2.putExtra("email", email)
            intent2.putExtra("selectedCourse", course)
            startActivity(intent2)
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            val intent3 = Intent(this, profile::class.java)
            intent3.putExtra("email", email)
            intent3.putExtra("selectedCourse", course)
            startActivity(intent3)
        }

        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            val intent4 = Intent(this, homepage::class.java)
            intent4.putExtra("email", email)
            intent4.putExtra("selectedCourse", course)
            startActivity(intent4)
        }

        // Set up subject list based on course
        val subjectList = when (course) {
            "BCA" -> listOf("Select Subject", "C Programming", "C++ Programming", "Java Programming", "Data Structure", "HTML/CSS")
            "MCA" -> listOf("Select Subject", "Machine Learning", "Dotnet Programming", "Advanced Java", "DSA", "Web Design")
            "BBA" -> listOf("Select Subject", "Accounting", "Marketing", "Finance", "Financial Management", "Business Law")
            "MBA" -> listOf("Select Subject", "Business Management", "Human Resources", "Entrepreneurship", "Marketing", "Communication")
            else -> listOf("Select Subject")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjectList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subjectSpinner.adapter = adapter
    }
}
