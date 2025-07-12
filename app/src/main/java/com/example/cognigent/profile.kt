package com.example.cognigent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class profile : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText

    private lateinit var btnEditName: Button
    private lateinit var btnSaveName: Button
    private lateinit var btnEditEmail: Button
    private lateinit var btnSaveEmail: Button
    private lateinit var btnEditPhone: Button
    private lateinit var btnSavePhone: Button
    private lateinit var btnEditCourse: Button
    private lateinit var btnSaveCourse: Button

    private lateinit var textCourse: TextView
    private lateinit var spinnerCourse: Spinner
    private lateinit var textName: TextView
    private lateinit var textEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        prefs = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

        val nameFromLogin = intent.getStringExtra("name")
        val emailFromLogin = intent.getStringExtra("email")

        if (!prefs.contains("name") && nameFromLogin != null) {
            prefs.edit().putString("name", nameFromLogin).apply()
        }
        if (!prefs.contains("email") && emailFromLogin != null) {
            prefs.edit().putString("email", emailFromLogin).apply()
        }

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        textCourse = findViewById(R.id.textCourse)
        textName = findViewById(R.id.textName)
        textEmail = findViewById(R.id.textEmail)
        spinnerCourse = findViewById(R.id.spinnerCourse)

        btnEditName = findViewById(R.id.btnEditName)
        btnSaveName = findViewById(R.id.btnSaveName)
        btnEditEmail = findViewById(R.id.btnEditEmail)
        btnSaveEmail = findViewById(R.id.btnSaveEmail)
        btnEditPhone = findViewById(R.id.btnEditPhone)
        btnSavePhone = findViewById(R.id.btnSavePhone)
        btnEditCourse = findViewById(R.id.btnEditCourse)
        btnSaveCourse = findViewById(R.id.btnSaveCourse)

        loadProfileData()
        setupEditSaveLogic(editName, btnEditName, btnSaveName, "name")
        setupEditSaveLogic(editEmail, btnEditEmail, btnSaveEmail, "email")
        setupEditSaveLogic(editPhone, btnEditPhone, btnSavePhone, "phone")
        setupCourseDropdown()

        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            intent.putExtra("course", prefs.getString("course", ""))
            startActivity(intent)
        }
    }

    private fun loadProfileData() {
        val name = prefs.getString("name", "")
        val email = prefs.getString("email", "")
        val phone = prefs.getString("phone", "")
        val course = prefs.getString("course", "Computer Science")

        editName.setText(name)
        editEmail.setText(email)
        editPhone.setText(phone)
        textCourse.text = course

        textName.text = "Name: $name"
        textEmail.text = "Email: $email"
    }

    private fun setupEditSaveLogic(editText: EditText, editBtn: Button, saveBtn: Button, key: String) {
        editBtn.setOnClickListener {
            editText.isEnabled = true
            animateView(editText, true)
        }

        saveBtn.setOnClickListener {
            val value = editText.text.toString().trim()
            if (value.isNotEmpty()) {
                prefs.edit().putString(key, value).apply()
                editText.isEnabled = false
                Toast.makeText(this, "$key saved", Toast.LENGTH_SHORT).show()
                loadProfileData()
            } else {
                Toast.makeText(this, "Enter valid $key", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCourseDropdown() {
        val courses = listOf("BCA", "MCA", "BBA", "MBA", "B.TECH", "M.TECH")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, courses)
        spinnerCourse.adapter = adapter

        val savedCourse = prefs.getString("course", "Computer Science") ?: "Computer Science"
        textCourse.text = savedCourse
        spinnerCourse.setSelection(courses.indexOf(savedCourse))

        btnEditCourse.setOnClickListener {
            animateView(textCourse, false)
            animateView(spinnerCourse, true)
        }

        btnSaveCourse.setOnClickListener {
            val selected = spinnerCourse.selectedItem.toString()
            prefs.edit().putString("course", selected).apply()
            textCourse.text = selected
            Toast.makeText(this, "Course saved", Toast.LENGTH_SHORT).show()
            animateView(spinnerCourse, false)
            animateView(textCourse, true)

            val intent = Intent(this, homepage::class.java)
            intent.putExtra("course", selected)
            startActivity(intent)
        }
    }

    private fun animateView(view: View, show: Boolean) {
        view.animate()
            .alpha(if (show) 1f else 0f)
            .setDuration(300)
            .withStartAction { if (show) view.visibility = View.VISIBLE }
            .withEndAction { if (!show) view.visibility = View.GONE }
            .start()
    }
}
