package com.example.coolflashcardapp

import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flash_question=findViewById<TextView>(R.id.flashcard_question)
        val flash_answer=findViewById<TextView>(R.id.flashcard_answer)


        flash_question.setOnClickListener{
            findViewById<View>(R.id.flashcard_answer).visibility = View.VISIBLE
            findViewById<View>(R.id.flashcard_question).visibility=View.INVISIBLE

            Toast.makeText(this, "Question button was clicked", Toast.LENGTH_SHORT)
        }

        flash_answer.setOnClickListener{
            findViewById<View>(R.id.flashcard_question).visibility=View.VISIBLE
            findViewById<View>(R.id.flashcard_answer).visibility=View.INVISIBLE


        }
    }
}