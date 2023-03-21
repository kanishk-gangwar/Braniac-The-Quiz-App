package com.example.quizapp.activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
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
import java.io.FileInputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val texts = arrayOf("Select Category","Science","GK","Maths","Social Science","English")
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    private var quizlist = mutableListOf<Quiz>()
    lateinit var firestore: FirebaseFirestore
    lateinit var btnProfile : Button
    lateinit var firebaseAuth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()



        Categoryclicks()
        setUpViews()
        click.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        Drawermenu.setOnClickListener {
            drawerlayout.openDrawer(GravityCompat.START)
        }

    }

    private fun Categoryclicks() {
        science.setOnClickListener {
            val myString = "Science"
            val intent = Intent(this, QuizAcitvity::class.java)
            intent.putExtra("MY_STRING", myString)
            startActivity(intent)
        }
        maths.setOnClickListener {
            val myString = "Maths"
            val intent = Intent(this, QuizAcitvity::class.java)
            intent.putExtra("MY_STRING", myString)
            startActivity(intent)

        }
        socialScience.setOnClickListener {
            val myString = "Social Science"
            val intent = Intent(this, QuizAcitvity::class.java)
            intent.putExtra("MY_STRING", myString)
            startActivity(intent)

        }
        gk.setOnClickListener {
            val myString = "GK"
            val intent = Intent(this, QuizAcitvity::class.java)
            intent.putExtra("MY_STRING", myString)
            startActivity(intent)

        }
        english.setOnClickListener {
            val myString = "English"
            val intent = Intent(this, QuizAcitvity::class.java)
            intent.putExtra("MY_STRING", myString)
            startActivity(intent)

        }

    }


    fun setUpViews() {
       setUpDrawerLayout()

    }


    private fun setUpDrawerLayout() {
         gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                // Swipe from left to right
                if (e1 != null && e2 != null && e1.x < e2.x && e2.x - e1.x > 125) {
                    drawerlayout.openDrawer(GravityCompat.START)
                }
                return super.onFling(e1!!, e2!!, velocityX, velocityY)
            }
        })

        drawerlayout.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

        toggle = ActionBarDrawerToggle(this, drawerlayout, 0, R.string.app_name)
        toggle.syncState()
        getdata()
        NavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btnLgOut -> {
                    firebaseAuth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.btnfollow -> Toast.makeText(applicationContext, "Follow us", Toast.LENGTH_SHORT).show()
                R.id.btnRate -> Toast.makeText(applicationContext, "Rate us", Toast.LENGTH_SHORT).show()
                R.id.btnLeaderBoard -> {
                    val intent = Intent(this, LeaderBoard::class.java)
                    startActivity(intent)
                }
                R.id.btnreview -> {
                    val intent = Intent(this,AdminLogin::class.java)
                    startActivity(intent)
                }
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

    fun CardView.setStroke(width: Int, color: Int) {
        val strokeWidth = resources.displayMetrics.density * width
        val strokeDrawable = GradientDrawable()
        strokeDrawable.setStroke(strokeWidth.toInt(), color)
        strokeDrawable.cornerRadius = radius
        val layers = arrayOf(strokeDrawable, background)
        val layerDrawable = LayerDrawable(layers)
        setBackground(layerDrawable)
    }



}