package com.example.cognigent

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class itemAdaptor(
    private val originalList: ArrayList<Subject>,
    private val context: Context
) : RecyclerView.Adapter<itemAdaptor.ViewHolder>() {

    private var filteredList: ArrayList<Subject> = ArrayList(originalList)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sPic: ImageView = view.findViewById(R.id.subjectpic)
        val sName: TextView = view.findViewById(R.id.subjectname)
        val sCode: TextView = view.findViewById(R.id.subjectcode)
        val card: CardView = view.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_subject_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subject = filteredList[position]
        holder.sPic.setImageResource(subject.image)
        holder.sName.text = subject.subjectName
        holder.sCode.text = subject.subjectCode

        holder.card.setOnClickListener {
            val intent = Intent(context, testpage::class.java)
            intent.putExtra("subjectName", subject.subjectName)
            intent.putExtra("subjectCode", subject.subjectCode)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredList = if (lowerCaseQuery.isEmpty()) {
            ArrayList(originalList)
        } else {
            ArrayList(originalList.filter {
                it.subjectName.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        it.subjectCode.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
            })
        }
        notifyDataSetChanged()
    }
}
