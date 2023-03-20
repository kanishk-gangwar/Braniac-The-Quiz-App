package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizapp.R
import com.example.quizapp.activities.adapters.QuizAdapter
import com.example.quizapp.activities.models.Quiz
import com.example.quizapp.activities.utils.QuestionActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_quiz_acitvity.*
import java.text.SimpleDateFormat
import java.util.*

class QuizAcitvity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    lateinit var adapter: QuizAdapter
    private var quizlist = mutableListOf<Quiz>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_acitvity)
        setUpViews()
    }


    fun setUpViews() {

        setUpRecyclerView()
        setUpFireStore()
        setUpDatePicker()
    }

    private var myString: String = ""


    private fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        myString = intent.getStringExtra("MY_STRING").toString()
        val collectionReference = firestore.collection(myString)
        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(this, "Error Fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA", value.toObjects(Quiz::class.java).toString())
            // quizlist.clear()
            quizlist.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpDatePicker() {
        btndatepicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)
                val dateformatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateformatter.format(Date(it))
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("DATE", date)
                intent.putExtra("MY_STRING", myString)
                startActivity(intent)
                Toast.makeText(this, "$date", Toast.LENGTH_SHORT).show()
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)
            }
            datePicker.addOnCancelListener {
                Log.d("DATEPICKER", "Date Picker Cancelled")
            }
        }

    }

    private fun setUpRecyclerView() {
        adapter = QuizAdapter(this, quizlist, object : QuizAdapter.OnQuizItemClickListener {
            override fun onQuizItemClick(title: String) {
                onQuizSelected(title)
            }
        })
     //   adapter.onQuizItemClickListener = this::onQuizSelected
       // adapter = QuizAdapter(this, quizlist)
        quizRecyclerview.layoutManager = GridLayoutManager(this, 2)
        quizRecyclerview.adapter = adapter


    }

    private fun onQuizSelected(title: String) {
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("DATE", title)
        intent.putExtra("MY_STRING", myString)
        startActivity(intent)
    }

}