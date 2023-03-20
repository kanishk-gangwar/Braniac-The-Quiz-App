package com.example.quizapp.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.activities.models.User

class LeaderboardAdapter(private var users: List<User>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    // ViewHolder for each item in the RecyclerView
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val scoreTextView: TextView = view.findViewById(R.id.scoreTextView)
        val rankTextView : TextView = view.findViewById(R.id.rankTextView)
    }

    // Create a ViewHolder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.leaderboard_item, parent, false)
        return ViewHolder(view)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.nameTextView.text = user.name
        holder.scoreTextView.text = user.score.toString()
        holder.rankTextView.text = user.rank
    }

    // Return the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return users.size
    }
}