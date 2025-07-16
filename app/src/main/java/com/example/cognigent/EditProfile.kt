package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

class EditProfile : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var oldEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        dbHelper = DBHelper(this)
        oldEmail = intent.getStringExtra("email") ?: ""

        val nameEdit = findViewById<EditText>(R.id.editName)
        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val courseEdit = findViewById<EditText>(R.id.editCourse)
        val passwordEdit = findViewById<EditText>(R.id.editText4)
        val saveBtn = findViewById<Button>(R.id.savebtn)
        val backIc = findViewById<TextView>(R.id.backIc)

        backIc.setOnClickListener {
            val intent = Intent(this, profile::class.java)
            intent.putExtra("email", oldEmail)
            startActivity(intent)
            finish()
        }

        // Load existing user info
        val user = dbHelper.getUserNameAndCourse(oldEmail)
        nameEdit.setText(user?.first ?: "")
        courseEdit.setText(user?.second ?: "")
        emailEdit.setText(oldEmail)
        passwordEdit.setText(dbHelper.getPassword(oldEmail) ?: "")

        // Optionally prefill password if you store it (not recommended for security)
        // passwordEdit.setText(...) — only if you have access to plain password, usually you don't.

        saveBtn.setOnClickListener {
            val newName = nameEdit.text.toString().trim()
            val newEmail = emailEdit.text.toString().trim()
            val newCourse = courseEdit.text.toString().trim()
            val newPassword = passwordEdit.text.toString().trim()

            if (newName.isBlank() || newEmail.isBlank() || newCourse.isBlank() || newPassword.isBlank()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prevent duplicate email
            if (oldEmail != newEmail && dbHelper.isEmailRegistered(newEmail)) {
                Toast.makeText(this, "This email is already in use", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updated = dbHelper.updateUserProfileWithEmailChange(
                oldEmail, newEmail, newName, newCourse, newPassword
            )

            if (updated) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, profile::class.java)
                intent.putExtra("email", newEmail)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
