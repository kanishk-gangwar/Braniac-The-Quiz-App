package com.example.quizapp.activities

import android.content.ContentValues
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.drawer_header.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    private var quizlist = mutableListOf<Quiz>()
    lateinit var firestore: FirebaseFirestore
    lateinit var btnProfile : Button
    lateinit var firebaseAuth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

        setUpViews()
        click.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

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
        getdata()
        NavigationView.setNavigationItemSelectedListener {


            when(it.itemId){
                R.id.btnLgOut -> {
                    firebaseAuth.signOut()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
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

    private fun getdata() {
        val text = firebaseAuth.currentUser!!.email
        val docRef = db.collection("User").document(text.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("Name")
                    val email = document.getString("Email")
                    UserName.text = name
                    Emailtxt.text = email
                    // Use the name field as needed
                } else {
                    Log.d(ContentValues.TAG, "User document does not exist on the database")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Failed to retrieve user document: $exception")
            }
    }


}