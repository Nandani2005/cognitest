package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

class EditProfile : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var oldEmail: String
    private lateinit var subjectSpinner: Spinner
    private lateinit var subjectList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        dbHelper = DBHelper(this)
        oldEmail = intent.getStringExtra("email") ?: ""

        val nameEdit = findViewById<EditText>(R.id.editName)
        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passwordEdit = findViewById<EditText>(R.id.editText4)
        val saveBtn = findViewById<Button>(R.id.savebtn)
        val backIc = findViewById<TextView>(R.id.backIc)

        // Set up static subject spinner
        subjectSpinner = findViewById(R.id.courseSpinner)
        subjectList = listOf(
            "Select Course", "BCA", "MCA", "BBA", "MBA"
        )
        val adapter = ArrayAdapter(this, R.layout.spinner_selected_item, subjectList)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        subjectSpinner.adapter = adapter

        // Get current user info
        val user = dbHelper.getUserNameAndCourse(oldEmail)
        val currentName = user?.first ?: ""
        val currentSubject = user?.second ?: ""

        nameEdit.setText(currentName)
        emailEdit.setText(oldEmail)
        passwordEdit.setText(dbHelper.getPassword(oldEmail) ?: "")

        // Pre-select spinner item to match current subject
        val subjectIndex = subjectList.indexOf(currentSubject)
        if (subjectIndex != -1) subjectSpinner.setSelection(subjectIndex)

        backIc.setOnClickListener {
            val intent = Intent(this, profile::class.java)
            intent.putExtra("email", oldEmail)
            startActivity(intent)
            finish()
        }

        saveBtn.setOnClickListener {
            val newName = nameEdit.text.toString().trim()
            val newEmail = emailEdit.text.toString().trim()
            val newSubject = subjectSpinner.selectedItem.toString()
            val newPassword = passwordEdit.text.toString().trim()

            if (newName.isBlank() || newEmail.isBlank() || newSubject == "Select Subject" || newPassword.isBlank()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (oldEmail != newEmail && dbHelper.isEmailRegistered(newEmail)) {
                Toast.makeText(this, "This email is already in use", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updated = dbHelper.updateUserProfileWithEmailChange(
                oldEmail, newEmail, newName, newSubject, newPassword
            )

            if (updated) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, profile::class.java)
                intent.putExtra("email", newEmail)
                intent.putExtra("newSubject" , newSubject)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
