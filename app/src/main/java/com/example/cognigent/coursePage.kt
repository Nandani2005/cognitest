package com.example.cognigent

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

class coursePage : AppCompatActivity() {

    private lateinit var selectedCourse: String
    private lateinit var dbHelper: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_page)

        val email = intent.getStringExtra("email") ?: ""
        dbHelper = DBHelper(this)

        // Fetch name from DB
        val user = dbHelper.getUserNameAndCourse(email)
        val name = user?.first ?: "User"

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val spinner = findViewById<Spinner>(R.id.courseSpinner)
        val proceedBtn = findViewById<Button>(R.id.proceedBtn)

        welcomeText.text = "Welcome, $name!"

        val courses = listOf("Select Course", "BCA", "MCA", "BBA", "MBA")
        val adapter = ArrayAdapter(this, R.layout.spinner_selected_item, courses)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

        selectedCourse = "Select Course"

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCourse = courses[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCourse = "Select Course"
            }
        }

        proceedBtn.setOnClickListener {
            if (selectedCourse == "Select Course") {
                Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show()
            } else {
                // Save selected course to DB
                val success = dbHelper.updateCourse(email, selectedCourse)
                if (success) {
                    val intent = Intent(this, homepage::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save course. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
