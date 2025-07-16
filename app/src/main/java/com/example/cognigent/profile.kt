package com.example.cognigent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

class profile : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        email = intent.getStringExtra("email") ?: ""
        dbHelper = DBHelper(this)

        val user = dbHelper.getUserNameAndCourse(email)

        findViewById<TextView>(R.id.profilename).text ="Name: " + user?.first
        findViewById<TextView>(R.id.profileemail).text = "Email: " + email
        findViewById<TextView>(R.id.profilecourse).text = "Course: " + user?.second

        findViewById<ImageView>(R.id.nav_progress).setOnClickListener {
            startActivity(Intent(this, progress::class.java).apply {
                putExtra("email", email)
            })
        }

        findViewById<ImageView>(R.id.nav_result).setOnClickListener {
            startActivity(Intent(this, notification::class.java).apply {
                putExtra("email", email)
            })
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            // Already in profile, optionally show a toast or do nothing
        }

        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, homepage::class.java).apply {
                putExtra("email", email)
            })
        }

        findViewById<TextView>(R.id.backIc).setOnClickListener {
            startActivity(Intent(this, homepage::class.java).apply {
                putExtra("email", email)
            })
        }

        findViewById<TextView>(R.id.editbtn).setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.logout).setOnClickListener {
            startActivity(Intent(this, loginpage::class.java))
            finish()
        }
    }
}
