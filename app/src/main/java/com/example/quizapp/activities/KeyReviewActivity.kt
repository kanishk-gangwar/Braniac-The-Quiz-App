package com.example.quizapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.activities.adapters.KeyAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_key_review.*
import kotlinx.android.synthetic.main.activity_profile.*

class KeyReviewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var srchbtn : Button
    private lateinit var selectedCategory : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_review)


        srchbtn = findViewById(R.id.keybtn)
        recyclerView = findViewById(R.id.keyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val data =  arrayOf("Select Category","Science","GK","Maths","Social Science","English")
        reviewspinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,data)

        reviewspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = data[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        srchbtn.setOnClickListener {
            recyclerViewData(selectedCategory)

        }




    }

    private fun recyclerViewData(Subject : String) {
        val ref = FirebaseDatabase.getInstance().getReference("Quizzes").child(Subject)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val keys = ArrayList<String>()
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    keys.add(key!!)
                }
                val adapter = KeyAdapter(keys,this@KeyReviewActivity,selectedCategory)
                Toast.makeText(this@KeyReviewActivity, selectedCategory,Toast.LENGTH_SHORT).show()
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }


}