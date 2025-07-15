package com.example.cognigent


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity



class notification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)
        val email = intent.getStringExtra("email") ?: ""

        findViewById<ImageView>(R.id.nav_progress).setOnClickListener {
            val intent1=Intent(this, progress::class.java)
            intent1.putExtra("email", email)
            startActivity(intent1)
        }

        findViewById<ImageView>(R.id.nav_result).setOnClickListener {
            val intent2=Intent(this, notification::class.java)
            intent2.putExtra("email", email)
            startActivity(intent2)        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            val intent3=Intent(this, profile::class.java)
            intent3.putExtra("email", email)
            startActivity(intent3)        }
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            val intent4=Intent(this, homepage::class.java)
            intent4.putExtra("email", email)
            startActivity(intent4)        }
    }
}