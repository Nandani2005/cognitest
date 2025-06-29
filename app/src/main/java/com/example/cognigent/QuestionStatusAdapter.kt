package com.example.cognigent// Change to your package name

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cognigent.R

class QuestionStatusAdapter(private val attempts: List<Attempt>) :
    RecyclerView.Adapter<QuestionStatusAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStatusIcon: ImageView = view.findViewById(R.id.iv_status_icon)
        val tvQuestionNumber: TextView = view.findViewById(R.id.tv_question_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question_status, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attempt = attempts[position]
        holder.tvQuestionNumber.text = "Question ${attempt.questionId}" // Assuming questionId starts from 1 or is sequential

        if (attempt.isCorrect) {
            holder.ivStatusIcon.setImageResource(R.drawable.green)
        } else {
            holder.ivStatusIcon.setImageResource(R.drawable.red)
        }
    }

    override fun getItemCount(): Int {
        return attempts.size
    }
}