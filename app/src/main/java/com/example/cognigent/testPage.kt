package com.example.cognigent

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import com.example.cognigent.btns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cognigent.R
import com.example.cognigent.TestAdapter
import com.example.cognigent.TestDatabaseHelper
import com.example.cognigent.profile
import com.example.cognigent.progress
import com.example.cognigent.solution


class testPage : AppCompatActivity() {
    lateinit var dbHelper: TestDatabaseHelper
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_page)

        dbHelper = TestDatabaseHelper(this)

        // Sample insert if no data (you can remove this in real app)
        if (dbHelper.getAllTests().isEmpty()) {
            dbHelper.insertTest("Math Test", "2025-06-29")
            dbHelper.insertTest("Science Test", "2025-06-30")
        }

        recyclerView = findViewById(R.id.testRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadTestData()
    }
    private fun loadTestData() {
        val testList = dbHelper.getAllTests()
        if (testList.isEmpty()) {
            Toast.makeText(this, "No tests found.", Toast.LENGTH_SHORT).show()
        } else {
            adapter = TestAdapter(testList)
            recyclerView.adapter = adapter
        }

        val test=findViewById<Button>(R.id.tests)
        val pBtn=findViewById<Button>(R.id.progress)
        val sol = findViewById<Button>(R.id.solution)
        val prof=findViewById<Button>(R.id.profile)

        test.setOnClickListener {
            val tr1= Intent(this,testPage::class.java)
            startActivity(tr1)
        }
        pBtn.setOnClickListener {
            val tr2= Intent(this,progress::class.java)
            startActivity(tr2)
        }
        sol.setOnClickListener {
            val tr3= Intent(this,solution::class.java)
            startActivity(tr3)
        }
        prof.setOnClickListener {
            val tr4= Intent(this,profile::class.java)
            startActivity(tr4)
        }

    }
}
