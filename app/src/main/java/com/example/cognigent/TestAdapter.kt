package com.example.cognigent

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class TestAdapter(private val testList: List<TestModel>) :
    RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    class TestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.testName)
        val date: TextView = view.findViewById(R.id.testDate)
        val startButton: Button = view.findViewById(R.id.startTestButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = testList[position]
        holder.name.text = test.name
        holder.date.text = test.date

        holder.startButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Starting ${test.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = testList.size
}


