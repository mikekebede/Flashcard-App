package com.example.coolflashcardapp

import android.content.Intent
//import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
            val answerSideView = findViewById<View>(R.id.flashcard_answer)

            // get the center for the clipping circle

            // get the center for the clipping circle
            val cx = answerSideView.width / 2
            val cy = answerSideView.height / 2

            // get the final radius for the clipping circle

            // get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // create the animator for this view (the start radius is zero)

            // create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius)

            // hide the question and show the answer to prepare for playing the animation!

            // hide the question and show the answer to prepare for playing the animation!

            flashAnswer.visibility = View.VISIBLE
            flashQuestion.visibility = View.INVISIBLE

            anim.duration = 300
            anim.start()




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
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
            Log.i("Yesigat" , "it was clicked")
        }
    val nextButton= findViewById<ImageView>(R.id.nextflash_button)
    nextButton.setOnClickListener {
        if(allflashcards.isEmpty()){
            return@setOnClickListener //early return so that the rest of the code deosnt execute

        }

        val leftOutAnim = AnimationUtils.loadAnimation(it.context, R.anim.left_out)
        val rightInAnim = AnimationUtils.loadAnimation(it.context, R.anim.right_in)

        leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // this method is called when the animation first starts
                flashAnswer.visibility=View.INVISIBLE
                flashQuestion.visibility=View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
                // this method is called when the animation is finished playing
                flashQuestion.startAnimation(rightInAnim)
                currentCardDisplayIndex++

                if(currentCardDisplayIndex>=allflashcards.size){
                    currentCardDisplayIndex=0
                }
                allflashcards=flashcardDatabase.getAllCards().toMutableList()
                val question=allflashcards[currentCardDisplayIndex].question

                val answer=allflashcards[currentCardDisplayIndex].answer

                flashQuestion.text = question
                flashAnswer.text = answer
                flashAnswer.visibility=View.INVISIBLE
                flashQuestion.visibility=View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // we don't need to worry about this method
            }

        })
        flashQuestion.startAnimation(leftOutAnim)

    }

    }

}