package com.example.cognigent

import android.R.attr.progress
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)
        val name = intent.getStringExtra("name")
        val course = intent.getStringExtra("course") ?: ""
        val textView = findViewById<TextView>(R.id.name)
        textView.text = "$name"
        findViewById<ImageView>(R.id.nav_progress).setOnClickListener {
            startActivity(Intent(this, progress::class.java))
        }

        findViewById<ImageView>(R.id.nav_result).setOnClickListener {
            startActivity(Intent(this, notification::class.java))
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }
         val c = findViewById<Button>(R.id.c)
        val cpp = findViewById<Button>(R.id.cpp)
        val java = findViewById<Button>(R.id.java)
        val dsa = findViewById<Button>(R.id.dsa)
        val dbms = findViewById<Button>(R.id.dbms)
        val html = findViewById<Button>(R.id.html)

        val javamca = findViewById<Button>(R.id.javamca)
        val python = findViewById<Button>(R.id.python)
        val oprsys = findViewById<Button>(R.id.oprsys)
        val networks= findViewById<Button>(R.id.networks)
        val ml = findViewById<Button>(R.id.ml)
        val mis = findViewById<Button>(R.id.mis)

        val buscom = findViewById<Button>(R.id.buscom)
        val marketing = findViewById<Button>(R.id.marketing)
        val finance = findViewById<Button>(R.id.finance)
        val hres = findViewById<Button>(R.id.hres)
        val eco = findViewById<Button>(R.id.eco)
        val acc = findViewById<Button>(R.id.acc)

        val maneco = findViewById<Button>(R.id.maneco)
        val hr= findViewById<Button>(R.id.hr)
        val blaw= findViewById<Button>(R.id.blaw)
        val busana = findViewById<Button>(R.id.busana)
        val bst= findViewById<Button>(R.id.bst)
        val fin = findViewById<Button>(R.id.fin)

        val bcaScroll = findViewById<ScrollView>(R.id.scrollSectionBCA)
        val mcaScroll = findViewById<ScrollView>(R.id.scrollSectionMCA)
        val bbaScroll = findViewById<ScrollView>(R.id.scrollSectionBBA)
        val mbaScroll = findViewById<ScrollView>(R.id.scrollSectionMBA)

        // Hide all scroll sections initially
        bcaScroll.visibility = View.GONE
        mcaScroll.visibility = View.GONE
        bbaScroll.visibility = View.GONE
        mbaScroll.visibility = View.GONE


        // Show based on course
        when (course) {
            "BCA" -> bcaScroll.visibility = View.VISIBLE
            "MCA" -> mcaScroll.visibility = View.VISIBLE
            "BBA" -> bbaScroll.visibility = View.VISIBLE
            "MBA" -> mbaScroll.visibility = View.VISIBLE
        }

        c.setOnClickListener(){
            val tr = Intent(this@homepage , testpage::class.java)
            startActivity(tr)
        }







    }
}