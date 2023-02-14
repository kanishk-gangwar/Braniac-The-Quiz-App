package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizapp.R
import com.example.quizapp.activities.adapters.QuizAdapter
import com.example.quizapp.activities.models.Quiz
import com.example.quizapp.activities.utils.QuestionActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    private var quizlist = mutableListOf<Quiz>()
    lateinit var firestore: FirebaseFirestore
    lateinit var btnProfile : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setUpViews()

    }

   fun setUpViews() {
       setUpDrawerLayout()
       setUpRecyclerView()
       setUpFireStore()
       setUpDatePicker()
    }

    private fun setUpDatePicker() {
          btndatepicker.setOnClickListener {
              val datePicker = MaterialDatePicker.Builder.datePicker().build()
              datePicker.show(supportFragmentManager,"DatePicker")
              datePicker.addOnPositiveButtonClickListener {
                  Log.d("DATEPICKER",datePicker.headerText)
                  val dateformatter = SimpleDateFormat("dd-MM-yyyy")
                  val date = dateformatter.format(Date(it))
                  val intent = Intent(this,QuestionActivity::class.java)
                  intent.putExtra("DATE",date)
                  startActivity(intent)
                  Toast.makeText(this,"$date",Toast.LENGTH_SHORT).show()
              }
              datePicker.addOnNegativeButtonClickListener {
                  Log.d("DATEPICKER",datePicker.headerText)
              }
              datePicker.addOnCancelListener {
                  Log.d("DATEPICKER","Date Picker Cancelled")
              }
          }

    }

    private fun setUpFireStore() {
       firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("Quizzes")
        collectionReference.addSnapshotListener { value, error ->
            if(value == null || error != null){
                Toast.makeText(this,"Error Fetching data",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA",value.toObjects(Quiz::class.java).toString())
           // quizlist.clear()
            quizlist.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpRecyclerView() {
              adapter = QuizAdapter(this,quizlist)
        quizRecyclerview.layoutManager = GridLayoutManager(this,2)
        quizRecyclerview.adapter = adapter

    }

    fun setUpDrawerLayout() {
        setSupportActionBar(appBar)
        toggle = ActionBarDrawerToggle(this,drawerlayout, R.string.app_name, R.string.app_name)
        toggle.syncState()
        NavigationView.setNavigationItemSelectedListener {


            when(it.itemId){
                R.id.btnprofile -> {val intent = Intent(this,ProfileActivity::class.java)
                    startActivity(intent)
                    drawerlayout.closeDrawers()
                }

                R.id.btnfollow -> Toast.makeText(applicationContext,"Follow us",Toast.LENGTH_SHORT).show()
                R.id.btnRate -> Toast.makeText(applicationContext,"Rate us",Toast.LENGTH_SHORT).show()
            }
            true

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}