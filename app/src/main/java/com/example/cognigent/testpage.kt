package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class testpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_testpage)
        val close = findViewById<Button>(R.id.closebtn)
        val start1 = findViewById<Button>(R.id.btn1)
        val start2 = findViewById<Button>(R.id.btn2)
        val start3 = findViewById<Button>(R.id.btn3)
        val trs = Intent(this , declaration::class.java)
        close.setOnClickListener {
            val tr = Intent(this, homepage::class.java)
            startActivity(tr)
        }
        start1.setOnClickListener {
            startActivity(trs)
        }
        start2.setOnClickListener {
            startActivity(trs)
        }
        start3.setOnClickListener {
            startActivity(trs)
        }

    }
}