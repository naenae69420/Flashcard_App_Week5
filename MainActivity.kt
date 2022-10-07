package com.example.flashcardnewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()

    var currCardDisplayedIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }
        flashcardQuestion.setOnClickListener {
            flashcardAnswer.visibility = View.VISIBLE
            flashcardQuestion.visibility = View.INVISIBLE
        }
        flashcardAnswer.setOnClickListener {
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }

        findViewById<View>(R.id.flashcard_answertwo).setOnClickListener {
            findViewById<View>(R.id.flashcard_answertwo).setBackgroundColor(
                getResources().getColor(
                    R.color.my_red_color,
                    null
                )
            )
        }
        findViewById<View>(R.id.flashcard_answerthree).setOnClickListener {
            findViewById<View>(R.id.flashcard_answerthree).setBackgroundColor(
                getResources().getColor(
                    R.color.my_red_color,
                    null
                )
            )
        }
        findViewById<View>(R.id.flashcard_answerfour).setOnClickListener {
            findViewById<View>(R.id.flashcard_answerfour).setBackgroundColor(
                getResources().getColor(
                    R.color.my_green_color,
                    null
                )
            )
        }

        findViewById<View>(R.id.delete_Btn).setOnClickListener {
            val flashcardQuestionToDelete = findViewById<TextView>(R.id.flashcard_question).text.toString()
            flashcardDatabase.deleteCard(flashcardQuestionToDelete)

                if (allFlashcards.isEmpty()) {
                    return@setOnClickListener
                }
                currCardDisplayedIndex++
                if (currCardDisplayedIndex >= allFlashcards.size) {
                    // go back to the beginning
                    currCardDisplayedIndex = 0
                }
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()

            }



        findViewById<View>(R.id.add_question_button).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        }

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val data: Intent? = result.data
                if (data != null) {
                    val questionString = data.getStringExtra("QUESTION_KEY")
                    val answerString = data.getStringExtra("ANSWER_KEY")

                    flashcardQuestion.text = questionString
                    flashcardAnswer.text = answerString

                    Log.i("MainActivity", "question: $questionString")
                    Log.i("MainActivity", "answer: $answerString")

               if (!questionString.isNullOrEmpty() && !answerString.isNullOrEmpty()) {
                   flashcardDatabase.insertCard(Flashcard(questionString, answerString))
                   allFlashcards = flashcardDatabase.getAllCards().toMutableList()
               }
                } else {
                    Log.i("MainActivity", "Returned null data from AddCardActivity")
                }
            }

        val addQuestionButton = findViewById<ImageView>(R.id.add_question_button)
        addQuestionButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

        val nextButton = findViewById<ImageView>(R.id.next_button)
        nextButton.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }
            currCardDisplayedIndex++
            if (currCardDisplayedIndex >= allFlashcards.size) {
                // go back to the beginning
                currCardDisplayedIndex = 0
            }
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()

            val question = allFlashcards[currCardDisplayedIndex].question
            val answer = allFlashcards[currCardDisplayedIndex].answer

            flashcardQuestion.text = question
            flashcardAnswer.text = answer
        }
    }
}

