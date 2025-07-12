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

        close.setOnClickListener {
            val tr = Intent(this, testpage::class.java)
            startActivity(tr)
        }
        startbtn.setOnClickListener {

            if (chkbox.isChecked == true) {
                val trf= Intent(this, QAPage::class.java)
                startActivity(trf)
            } else {
                Toast.makeText(this, "Please tick the CheckBox", Toast.LENGTH_SHORT).show()
            }
        }
    }
}