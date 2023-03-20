package com.example.quizapp.activities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.activities.adapters.LeaderboardAdapter
import com.example.quizapp.activities.models.User
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LeaderBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        val db = Firebase.firestore
        val usersRef = db.collection("User")

        usersRef.orderBy("score", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Create a list of User objects from the query results
                val users = querySnapshot.documents.map { document ->
                    val name = document.getString("Name") ?: ""
                    val score = document.getLong("score")?.toInt() ?: 0
                    User(name, score,"")
                }
                var rank = 1
                var prevScore = Int.MAX_VALUE
                users.forEach { user ->
                    val userRank = if (user.score < prevScore) rank++ else rank
                    prevScore = user.score
                    //user.rank = userRank
                   // user.rank = {if (userRank < 10) "0" else ""}$userRank
                if(userRank<10){
                    user.rank = ("0$userRank")
                }
                }

                // Display the leaderboard using the list of users
                displayLeaderboard(users)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting leaderboard", exception)
            }

    }


    // Function to display the leaderboard
    fun displayLeaderboard(users: List<User>) {
        // Create a RecyclerView to display the leaderboard
        val recyclerView = findViewById<RecyclerView>(R.id.leaderboardRecyclerView)

        // Create an adapter for the RecyclerView
        val adapter = LeaderboardAdapter(users)

        // Set the adapter on the RecyclerView
        recyclerView.adapter = adapter

        // Set the layout manager on the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        var rank = 1
//        var prevScore = Int.MAX_VALUE
//        users.forEach { user ->
//            val userRank = if (user.score < prevScore) rank++ else rank
//            prevScore = user.score
//            user.rank = userRank
//        }
//        userrank = rank
        }
    }
