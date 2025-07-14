package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

class forgetPassword : AppCompatActivity() {

    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        dbHelper = DBHelper(this) // Initialize DBHelper

        val back = findViewById<TextView>(R.id.back)
        val emailInput = findViewById<EditText>(R.id.mail)
        val getOtpBtn = findViewById<Button>(R.id.getotp)
        val otpLayout = findViewById<LinearLayout>(R.id.otpLayout)
        val verifyBtn = findViewById<Button>(R.id.verify)
        val timer = findViewById<TextView>(R.id.timer)

        otpLayout.visibility = View.GONE

        back.setOnClickListener {
            startActivity(Intent(this, loginpage::class.java))
        }

        getOtpBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.isEmailRegistered(email)) {
                otpLayout.visibility = View.VISIBLE
                getOtpBtn.visibility = View.GONE

                object : CountDownTimer(30000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timer.text = "Time left: ${millisUntilFinished / 1000}s"
                    }

                    override fun onFinish() {
                        getOtpBtn.visibility = View.VISIBLE
                        otpLayout.visibility = View.GONE
                    }
                }.start()

            } else {
                Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show()
            }
        }

        verifyBtn.setOnClickListener {

            // Simulate successful verification, go to reset password page
           // val email = emailInput.text.toString().trim()
            //val intent = Intent(this, ResetPassword::class.java)
            //intent.putExtra("email", email)
            //startActivity(intent)
        }
    }
}
