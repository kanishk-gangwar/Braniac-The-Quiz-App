package com.example.quizapp.activities.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.activities.AdminActivity
import com.google.firebase.database.FirebaseDatabase

class KeyAdapter(private val keys: List<String>,private val Context : Context,private val Category : String) :
    RecyclerView.Adapter<KeyAdapter.KeyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.quizpallete, parent, false)
        return KeyViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeyViewHolder, position: Int) {
        val key = keys[position]
 //       holder.textViewKey.text = key
        val quizNumber = "Quiz " + (position + 1)
        holder.textViewKey.text = quizNumber
        holder.itemView.setOnClickListener {
            // Pass the key to the next activity or fragment
            val intent = Intent(Context,AdminActivity::class.java)
            intent.putExtra("key", key)
            intent.putExtra("Category",Category)
            Context.startActivity(intent)
        }
        holder.btndelete.setOnClickListener {
            val databaseRef = FirebaseDatabase.getInstance().getReference("Quizzes").child(Category)
            databaseRef.child(key).removeValue().addOnSuccessListener {
                Toast.makeText(Context,"Quiz Removed Successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                    Toast.makeText(Context,"Something went wrong!!",Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int {
        return keys.size
    }

    class KeyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewKey: TextView = itemView.findViewById(R.id.key_id)
        var btndelete : ImageButton = itemView.findViewById(R.id.removebtn)
    }
}
