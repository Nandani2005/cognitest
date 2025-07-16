package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

class loginpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        val dbHelper = DBHelper(this)

        val btnLogin = findViewById<TextView>(R.id.loginbtn)
        val SignUptab = findViewById<TextView>(R.id.signupbtn)
        val loginUnderline = findViewById<View>(R.id.loginUnderline)
        val signupUnderline = findViewById<View>(R.id.signupUnderline)

        val loginLayout = findViewById<LinearLayout>(R.id.signInLayout)
        val signUpLayout = findViewById<LinearLayout>(R.id.signUpLayout)

        loginLayout.visibility = View.VISIBLE
        signUpLayout.visibility = View.GONE
        loginUnderline.visibility = View.VISIBLE
        signupUnderline.visibility = View.GONE

        btnLogin.setOnClickListener {
            loginLayout.visibility = View.VISIBLE
            signUpLayout.visibility = View.GONE
            loginUnderline.visibility = View.VISIBLE
            signupUnderline.visibility = View.GONE
        }

        SignUptab.setOnClickListener {
            loginLayout.visibility = View.GONE
            signUpLayout.visibility = View.VISIBLE
            loginUnderline.visibility = View.GONE
            signupUnderline.visibility = View.VISIBLE
        }

        val loginEmail = findViewById<EditText>(R.id.logInEmail)
        val loginPassword = findViewById<EditText>(R.id.logInPassword)
        val login = findViewById<Button>(R.id.logIn)

        login.setOnClickListener {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()
            if(email.equals("admin@gmail.com")){
                val intent = Intent(this, AddQuestionActivity::class.java)
                startActivity(intent)
                finish()
            }

            else if (dbHelper.checkLogin(email, password)) {
                val user = dbHelper.getUserNameAndCourse(email)
                val intent = Intent(this, coursePage::class.java)
                intent.putExtra("name", user?.first)
                intent.putExtra("email", user?.second)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show()
            }
        }

        val signUp = findViewById<Button>(R.id.signUp)
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.signUpEmail)
        val pass = findViewById<EditText>(R.id.signUpPassword)
        val confirm = findViewById<EditText>(R.id.signUpPasswordCon)

        signUp.setOnClickListener {
            if (name.text.isEmpty() || email.text.isEmpty() || pass.text.isEmpty() || confirm.text.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (pass.text.toString() != confirm.text.toString()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertUser(name.text.toString(), email.text.toString(), pass.text.toString())
                if (success) {
                    val intent = Intent(this, coursePage::class.java)
                    intent.putExtra("name", name.text.toString())
                    intent.putExtra("email", email.text.toString())
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val forget= findViewById<TextView>(R.id.forgetpass)
        forget.setOnClickListener {
            val password = Intent(this, forgetPassword::class.java)
            startActivity(password)
        }
    }
}