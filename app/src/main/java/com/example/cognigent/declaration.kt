package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class declaration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_declaration)
        val close= findViewById<Button>(R.id.closetab)
        val chkbox = findViewById<CheckBox>(R.id.checkbox)
        val startbtn = findViewById<Button>(R.id.startbtn)
        val testNo = intent.getStringExtra("testNo")

        close.setOnClickListener {
           finish()
        }
        startbtn.setOnClickListener {

            if (chkbox.isChecked == true) {
                val trf= Intent(this, QAPage::class.java)
                trf.putExtra("testNO", testNo)
                startActivity(trf)
            } else {
                Toast.makeText(this, "Please tick the CheckBox", Toast.LENGTH_SHORT).show()
            }
        }
    }
}