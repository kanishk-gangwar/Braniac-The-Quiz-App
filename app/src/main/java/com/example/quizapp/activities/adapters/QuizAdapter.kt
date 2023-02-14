package com.example.quizapp.activities.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.activities.models.Quiz
import com.example.quizapp.activities.utils.ColorPicker
import com.example.quizapp.activities.utils.IconPicker
import com.example.quizapp.activities.utils.QuestionActivity

class QuizAdapter(val context : Context, val quizzes : List<Quiz>):RecyclerView.Adapter<QuizAdapter.QuizViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quiz_item,parent,false)
        return QuizViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.textTitle.text = quizzes[position].title
        holder.cardcontainer.setCardBackgroundColor(Color.parseColor(ColorPicker.getcolor()))
        holder.iconView.setImageResource(IconPicker.getIcons())
        holder.itemView.setOnClickListener {
            Toast.makeText(context,quizzes[position].title,Toast.LENGTH_SHORT).show()
            val intent = Intent(context,QuestionActivity::class.java)
            intent.putExtra("DATE",quizzes[position].title)
            context.startActivity(intent)

        }
    }
    inner class QuizViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        var textTitle : TextView = itemview.findViewById(R.id.quizTitle)
        var iconView : ImageView = itemview.findViewById(R.id.quizIcon)
        var cardcontainer : CardView = itemview.findViewById(R.id.cardcontainer)
    }

}