package com.example.cognigent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cognigent.QuestionDatabase
import com.example.cognigent.QuestionModel
import com.example.cognigent.R

class PreviewAdapter(
    private val questionList: List<QuestionModel>

) : RecyclerView.Adapter<PreviewAdapter.ViewHolder>() {
    var marks=0;
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val optionImg: ImageView = view.findViewById(R.id.resultIcon)
        val qNo: TextView = view.findViewById(R.id.questionNumber)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questionList[position]
        holder.qNo.text = "${position + 1}"


        when {
            question.Attemp == -1 -> {
                holder.optionImg.setImageResource(R.drawable.ic_cross)
            }
            question.Attemp == question.correctIndex -> {

                holder.optionImg.setImageResource(R.drawable.ic_check)
                marks++;
            }
            else -> {
                holder.optionImg.setImageResource(R.drawable.ic_check)
            }
        }
    }

    override fun getItemCount(): Int = questionList.size
}




