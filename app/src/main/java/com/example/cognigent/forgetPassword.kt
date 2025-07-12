package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class forgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)


        val intent = Intent(this@forgetPassword, loginpage::class.java)
        val back = findViewById<TextView>(R.id.back)
        back.setOnClickListener {
            startActivity(intent)
        }
        val lay = findViewById<LinearLayout>(R.id.otpLayout)
        lay.visibility = LinearLayout.GONE
        val GetOtp = findViewById<Button>(R.id.getotp)
        GetOtp.setOnClickListener {
            lay.visibility = LinearLayout.VISIBLE
            GetOtp.visibility = Button.GONE
            val timer = findViewById<TextView>(R.id.timer)
            object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timer.text = (millisUntilFinished / 1000).toString()
                }
                override fun onFinish() {
                    GetOtp.visibility = Button.VISIBLE
                    lay.visibility = LinearLayout.GONE
                }
            }.start()
        }


        val Verify = findViewById<Button>(R.id.verify)
        Verify.setOnClickListener {
            startActivity(intent)
        }
    }
}