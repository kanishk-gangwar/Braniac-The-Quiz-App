package com.example.quizapp.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.activities.models.Questions

class reviewAdapter(private val questions: List<Questions>) : RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adminreview, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questions.size
}

class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val descriptionTextView: TextView = itemView.findViewById(R.id.questiontxt)
    private val option1TextView: TextView = itemView.findViewById(R.id.option1txt)
    private val option2TextView: TextView = itemView.findViewById(R.id.option2txt)
    private val option3TextView: TextView = itemView.findViewById(R.id.option3txt)
    private val option4TextView: TextView = itemView.findViewById(R.id.option4txt)
    private val correctanswerTextView: TextView = itemView.findViewById(R.id.correcttxt)

        fun bind(question: Questions) {
            descriptionTextView.text = question.description
            option1TextView.text = question.option1
            option2TextView.text = question.option2
            option3TextView.text = question.option3
            option4TextView.text = question.option4
            correctanswerTextView.text = question.answer
        }
}
