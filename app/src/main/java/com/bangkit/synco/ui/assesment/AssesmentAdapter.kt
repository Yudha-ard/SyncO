package com.bangkit.synco.ui.assessment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.R
class AssessmentAdapter(private val questions: Array<String>) :
    RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    private val selectedQuestions = ArrayList<String>() // Use a Set to store selected questions

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button1: ToggleButton = itemView.findViewById(R.id.item_button)

        init {
            button1.setOnCheckedChangeListener { _, isChecked ->
                val question = questions[adapterPosition]
                if (isChecked) {
                    // Question is selected
                    selectedQuestions.add(question)
                } else {
                    // Question is unselected
                    selectedQuestions.remove(question)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.button1.text = question.replace("_", " ")
        holder.button1.textOn = question.replace("_", " ")
        holder.button1.textOff = question.replace("_", " ")
        holder.button1.isChecked = selectedQuestions.contains(question)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun getSelectedQuestions(): ArrayList<String> {
        Log.d("assesmentActivity", "$selectedQuestions")
        return selectedQuestions
    }
}
