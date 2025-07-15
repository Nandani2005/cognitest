package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.DBHelper

class forgetPassword : AppCompatActivity() {

    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        dbHelper = DBHelper(this)

        val back = findViewById<TextView>(R.id.back)
        val emailInput = findViewById<EditText>(R.id.mail)
        val getOtpBtn = findViewById<Button>(R.id.getotp)
        val otpLayout = findViewById<LinearLayout>(R.id.otpLayout)
        val verifyBtn = findViewById<Button>(R.id.verify)
        val timer = findViewById<TextView>(R.id.timer)
        val otpInput = findViewById<EditText>(R.id.otp)
        val resetBtn = findViewById<Button>(R.id.backtologin)
        val resetPasswordLayout = findViewById<LinearLayout>(R.id.resetpassword)
        val emailLayout = findViewById<LinearLayout>(R.id.editEmail)
        val newPassword = findViewById<EditText>(R.id.resetpass)
        val confirmPassword = findViewById<EditText>(R.id.confirmpass)

        otpLayout.visibility = View.GONE
        resetPasswordLayout.visibility = View.GONE

        back.setOnClickListener {
            startActivity(Intent(this, loginpage::class.java))
            finish()
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
                        Toast.makeText(this@forgetPassword, "OTP expired. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }.start()

            } else {
                Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show()
            }
        }

        verifyBtn.setOnClickListener {
            val otp = otpInput.text.toString().trim()
            if (otp.isEmpty()) {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulate OTP always correct
            emailLayout.visibility = View.GONE
            resetPasswordLayout.visibility = View.VISIBLE
            otpLayout.visibility = View.GONE
        }

        resetBtn.setOnClickListener {
            val password = newPassword.text.toString().trim()
            val confirm = confirmPassword.text.toString().trim()
            val email = emailInput.text.toString().trim()

            if (password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please enter both password fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updated = dbHelper.updatePassword(email, password)

            if (updated) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, loginpage::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
