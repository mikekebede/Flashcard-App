package com.example.coolflashcardapp

import android.content.Intent
//import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
//import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allflashcards= mutableListOf<Flashcard>()
    var currentCardDisplayIndex=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase= FlashcardDatabase(this)
        allflashcards= flashcardDatabase.getAllCards().toMutableList()

        val flashQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashAnswer = findViewById<TextView>(R.id.flashcard_answer)

        if(allflashcards.size>0){
            flashQuestion.text=allflashcards[0].question
            flashAnswer.text=allflashcards[0].answer
        }
        flashQuestion.setOnClickListener {
            flashAnswer.visibility = View.VISIBLE
            flashQuestion.visibility = View.INVISIBLE

            Toast.makeText(this, "Question button was clicked", Toast.LENGTH_SHORT).show()
        }
        flashAnswer.setOnClickListener {
           flashQuestion.visibility = View.VISIBLE
            flashAnswer.visibility = View.INVISIBLE
            Log.i("Yessir" , "it was clicked")

        }


       val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            val data: Intent?=result.data


            if(data!= null){
                val questionString=data.getStringExtra("QUESTION_KEY")
                val answerString=data.getStringExtra("ANSWER_KEY")

                flashQuestion.text=questionString
                flashAnswer.text=answerString


                Log.i( "Mike :MainActivity", "Question : $questionString" )
                Log.i( "Mike :MainActivity", "Question : $answerString" )
                if(!questionString.isNullOrEmpty() && !answerString.isNullOrEmpty()) {
                    flashcardDatabase.insertCard(Flashcard(questionString, answerString))
                    allflashcards=flashcardDatabase.getAllCards().toMutableList()
                }
            }
            else{
                Log.i( "Mike :MainActivity", "returned null from AddCardActivity" )
            }
        }

        val addQuestionButton=findViewById<ImageView>(R.id.add_question_button)
        addQuestionButton.setOnClickListener{
            val intent=Intent(this , AddCardActivity::class.java)
            resultLauncher.launch(intent)

            Log.i("Yesigat" , "it was clicked")
        }
    val nextButton= findViewById<ImageView>(R.id.nextflash_button)
    nextButton.setOnClickListener {
        if(allflashcards.isEmpty()){
            return@setOnClickListener //early return so that the rest of the code deosnt execute

        }
        currentCardDisplayIndex++

        if(currentCardDisplayIndex>=allflashcards.size){
            currentCardDisplayIndex=0
        }
        allflashcards=flashcardDatabase.getAllCards().toMutableList()
        val question=allflashcards[currentCardDisplayIndex].question

        val answer=allflashcards[currentCardDisplayIndex].answer

        flashQuestion.text = question
        flashAnswer.text = answer
    }

    }

}