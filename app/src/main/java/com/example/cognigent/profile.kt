package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        val test=findViewById<Button>(R.id.tests)
        val pBtn=findViewById<Button>(R.id.progress)
        val sol = findViewById<Button>(R.id.solution)
        val prof=findViewById<Button>(R.id.profile)

        test.setOnClickListener {
            val tr1= Intent(this,testPage::class.java)
            startActivity(tr1)
            finish()
        }
        pBtn.setOnClickListener {
            val tr2= Intent(this,progress::class.java)
            startActivity(tr2)
            finish()
        }
        sol.setOnClickListener {
            val tr3= Intent(this,solution::class.java)
            startActivity(tr3)
            finish()
        }
        prof.setOnClickListener {
            val tr4= Intent(this,profile::class.java)
            startActivity(tr4)
            finish()
        }

    }
}