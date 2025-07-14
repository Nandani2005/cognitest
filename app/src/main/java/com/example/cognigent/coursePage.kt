package com.example.cognigent

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class coursePage : AppCompatActivity() {

    private lateinit var selectedCourse: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_page)

        val name = intent.getStringExtra("name") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val spinner = findViewById<Spinner>(R.id.courseSpinner)
        val proceedBtn = findViewById<Button>(R.id.proceedBtn)


        welcomeText.text = "Welcome, $name!"

        val courses = listOf("Select Course", "BCA", "MCA", "BBA", "MBA" )
        val Adapter = ArrayAdapter(this, R.layout.spinner_selected_item, courses)
        Adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = Adapter



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
                val intent = Intent(this, homepage::class.java)
                intent.putExtra("name", name)
                intent.putExtra("email", email)
                intent.putExtra("course", selectedCourse)
                startActivity(intent)
                finish()
            }
        }
    }
}