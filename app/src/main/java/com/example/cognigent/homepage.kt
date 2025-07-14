package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class homepage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var items: itemAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)
        // Subject data
        val bcaSubject = arrayListOf(
            Subject(R.drawable.c_logo, "C Programming", "BCA101"),
            Subject(R.drawable.cpp_logo, "C++ Programming", "BCA102"),
            Subject(R.drawable.java_logo, "Java Programming", "BCA103"),
            Subject(R.drawable.ds_logo, "Data Structure", "BCA104"),
            Subject(R.drawable.html_logo, "HTML/CSS", "BCA105")
        )

        val mcaSubject = arrayListOf(
            Subject(R.drawable.c_logo, "Machine Learning", "MCA101"),
            Subject(R.drawable.cpp_logo, "Dotnet Programming", "MCA102"),
            Subject(R.drawable.java_logo, "Advanced Java", "MCA103"),
            Subject(R.drawable.ds_logo, "DSA", "MCA104"),
            Subject(R.drawable.html_logo, "Web Design", "MCA105")
        )

        val bbaSubject = arrayListOf(
            Subject(R.drawable.c_logo, "Accounting", "BBA101"),
            Subject(R.drawable.cpp_logo, "Marketing", "BBA102"),
            Subject(R.drawable.java_logo, "Finance", "BBA103"),
            Subject(R.drawable.ds_logo, "Financial Management", "BBA104"),
            Subject(R.drawable.html_logo, "Business Law", "BBA105")
        )

        val mbaSubject = arrayListOf(
            Subject(R.drawable.c_logo, "Business Management", "MBA101"),
            Subject(R.drawable.cpp_logo, "Human Resources", "MBA102"),
            Subject(R.drawable.java_logo, "Entrepreneurship", "MBA103"),
            Subject(R.drawable.ds_logo, "Marketing", "MBA104"),
            Subject(R.drawable.html_logo, "Communication", "MBA105")
        )

        // Get intent data
        val name = intent.getStringExtra("name")
        val course = intent.getStringExtra("course") ?: ""
        val Scourse = course.uppercase()
        val nameView=name.toString()
        // Set welcome name
        val textView = findViewById<TextView>(R.id.username)
        textView.text = "$nameView"

        // Navigation buttons
        findViewById<ImageView>(R.id.nav_progress).setOnClickListener {
            startActivity(Intent(this, progress::class.java))
        }

        findViewById<ImageView>(R.id.nav_result).setOnClickListener {
            startActivity(Intent(this, notification::class.java))
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }

        // Toggle search box visibility


        // Setup RecyclerView
        recyclerView = findViewById(R.id.ViewSubjects)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set adapter based on course
        items = when (Scourse) {
            "BCA" -> itemAdaptor(bcaSubject, this)
            "MCA" -> itemAdaptor(mcaSubject, this)
            "BBA" -> itemAdaptor(bbaSubject, this)
            "MBA" -> itemAdaptor(mbaSubject, this)
            else -> itemAdaptor(arrayListOf(), this)
        }

        recyclerView.adapter = items
        val searchBtn = findViewById<ImageView>(R.id.search)
        val searchBox = findViewById<EditText>(R.id.search_box)
        searchBox.visibility=View.GONE
        searchBtn.setOnClickListener {
            searchBox.visibility =
                if (searchBox.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        searchBox.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                items.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

}
