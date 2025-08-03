package com.example.cognigent

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.DBHelper

class homepage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var items: itemAdaptor
    private lateinit var dbHelper: DBHelper
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        dbHelper = DBHelper(this)
        prefs = getSharedPreferences("UserProfile", MODE_PRIVATE)

        // ✅ Get data from Intent or SharedPreferences
        var email = intent.getStringExtra("email") ?: prefs.getString("email", "") ?: ""
        val selectedCourseIntent = intent.getStringExtra("selectedCourse")
        val user = dbHelper.getUserNameAndCourse(email)

        val name = user?.first ?: prefs.getString("name", "User")!!
        val course = user?.second?.uppercase() ?: selectedCourseIntent?.uppercase()
        ?: prefs.getString("course", "")!!

        // ✅ Save fallback data for persistence
        prefs.edit()
            .putString("email", email)
            .putString("name", name)
            .putString("course", course)
            .apply()

        // ✅ Display name
        findViewById<TextView>(R.id.username).text = name

        // ✅ Setup navigation buttons
        findViewById<ImageView>(R.id.nav_progress).setOnClickListener {
            val intent1 = Intent(this, progress::class.java)
            intent1.putExtra("email", email)
            intent1.putExtra("selectedCourse", course)
            startActivity(intent1)
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            val intent3 = Intent(this, profile::class.java)
            intent3.putExtra("email", email)
            startActivity(intent3)
        }

        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            // Refresh homepage without losing user data
            val intent4 = Intent(this, homepage::class.java)
            startActivity(intent4)
            finish()
        }

        // ✅ Build subject list based on course
        val subjectList = when (course) {
            "BCA" -> listOf(
                Subject(R.drawable.c_logo, "C Programming", "BCA101"),
                Subject(R.drawable.cpp_logo, "C++ Programming", "BCA102"),
                Subject(R.drawable.java_logo, "Java Programming", "BCA103"),
                Subject(R.drawable.ds_logo, "Data Structure", "BCA104"),
                Subject(R.drawable.html_logo, "HTML/CSS", "BCA105")
            )
            "MCA" -> listOf(
                Subject(R.drawable.ml_logo, "Machine Learning", "MCA101"),
                Subject(R.drawable.dotnet_logo, "Dotnet Programming", "MCA102"),
                Subject(R.drawable.java_logo, "Advanced Java", "MCA103"),
                Subject(R.drawable.dsa_logo, "DSA", "MCA104"),
                Subject(R.drawable.webdesign_logo, "Web Design", "MCA105")
            )
            "BBA" -> listOf(
                Subject(R.drawable.acc_logo, "Accounting", "BBA101"),
                Subject(R.drawable.marketing_logo, "Marketing", "BBA102"),
                Subject(R.drawable.finance_logo, "Finance", "BBA103"),
                Subject(R.drawable.fin_mangement, "Financial Management", "BBA104"),
                Subject(R.drawable.bin_law_logo, "Business Law", "BBA105")
            )
            "MBA" -> listOf(
                Subject(R.drawable.bus_man_logo, "Business Management", "MBA101"),
                Subject(R.drawable.bus_man_logo, "Human Resources", "MBA102"),
                Subject(R.drawable.java_logo, "Entrepreneurship", "MBA103"),
                Subject(R.drawable.ds_logo, "Marketing", "MBA104"),
                Subject(R.drawable.html_logo, "Communication", "MBA105")
            )
            else -> listOf()
        }

        // ✅ Setup RecyclerView
        recyclerView = findViewById(R.id.ViewSubjects)
        recyclerView.layoutManager = LinearLayoutManager(this)
        items = itemAdaptor(ArrayList(subjectList), this)
        recyclerView.adapter = items

        // ✅ Setup search
        val searchBox = findViewById<EditText>(R.id.search_box)
        val searchBtn = findViewById<ImageView>(R.id.search)
        searchBox.visibility = View.GONE

        searchBtn.setOnClickListener {
            if (searchBox.visibility == View.GONE) {
                searchBox.visibility = View.VISIBLE
                searchBox.requestFocus()
            } else {
                searchBox.visibility = View.GONE
                hideKeyboard(searchBox)
            }
        }

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                items.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
