package com.example.cognigent

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PreviewAdapter(
    private val activity: Activity ,// To return result
    private var array: IntArray
) : RecyclerView.Adapter<PreviewAdapter.ViewHolder>() {

    // Fetch all questions from the database
    val dbHelper = QuestionDatabase(this.activity)
    private val questionList = dbHelper.sampleQuestions

     class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val optionImg: ImageView = view.findViewById(R.id.resultIcon)
        val cardHolder: View = view.findViewById(R.id.cardholder)
        val qNo: TextView = view.findViewById(R.id.questionNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questionList[position]

        // Set question number
        holder.qNo.text = "${position + 1}"

        if(array[position]==1){
            holder.optionImg.setImageResource(R.drawable.ic_check)   // ✅
        }else{
            holder.optionImg.setImageResource(R.drawable.ic_cross)   // ❌
        }

//        if (question.Attemp==1) {
//            holder.optionImg.setImageResource(R.drawable.ic_check)   // ✅
//        } else {
//            holder.optionImg.setImageResource(R.drawable.ic_cross)   // ❌
//        }



        // 4️⃣  When tapped, jump back to that question in QAPage
        holder.cardHolder.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("selected_question_index", position)
            }
            activity.setResult(Activity.RESULT_OK, resultIntent)
            activity.finish()
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}
