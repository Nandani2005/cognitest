package com.example.cognigent

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class testpage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_testpage)
        val close = findViewById<TextView>(R.id.closebtn)
        val start1 = findViewById<Button>(R.id.btn1)
        val start2 = findViewById<Button>(R.id.btn2)
        val start3 = findViewById<Button>(R.id.btn3)
        val s_name=findViewById<TextView>(R.id.Sname)
        s_name.text=("Subject:${intent.getStringExtra("subjectName")}")
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