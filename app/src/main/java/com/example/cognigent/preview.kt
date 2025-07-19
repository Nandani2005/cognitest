package com.example.cognigent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cognigent.PreviewAdapter
import com.example.cognigent.QuestionModel
import com.example.cognigent.QuestionDatabase

class preview : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backBtn: Button
    private lateinit var questionList: ArrayList<QuestionModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)


        recyclerView = findViewById(R.id.reviewRecyclerView)
        backBtn = findViewById(R.id.backBtn)

        val dbHelper = QuestionDatabase(this)
        questionList = dbHelper.getQuestionsByExamType("mba") as ArrayList<QuestionModel>

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = PreviewAdapter(questionList)


        backBtn.setOnClickListener {
            finish()
        }
    }
}
