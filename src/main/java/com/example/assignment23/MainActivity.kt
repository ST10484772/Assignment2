package com.example.assignment23

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val flashcards = listOf(
        "Nelson Mandela was president in 1994?" to true,
        "WWII ended in 1940?" to false,
        "The Berlin Wall fell in 1989?" to true,
        "The Renaissance began in the 12th century?" to false,
        "The US declared independence in 1776?" to true
    )

    private var currentIndex = 0
    private var score = 0
    private val userAnswers = MutableList<Boolean?>(flashcards.size) { null }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWelcomeScreen()
    }

    private fun showWelcomeScreen() {
        setContentView(R.layout.welcome_screen)
        findViewById<Button>(R.id.startButton).setOnClickListener {
            resetQuiz()
            showQuestionScreen()
        }
    }

    private fun resetQuiz() {
        currentIndex = 0
        score = 0
        userAnswers.fill(null)
    }

    private fun showQuestionScreen() {
        setContentView(R.layout.quiz_screen)
        displayCurrentQuestion()

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.isEnabled = false

        findViewById<Button>(R.id.trueButton).setOnClickListener {
            recordAnswer(true)
            nextButton.isEnabled = true
        }

        findViewById<Button>(R.id.falseButton).setOnClickListener {
            recordAnswer(false)
            nextButton.isEnabled = true
        }

        nextButton.setOnClickListener {
            currentIndex++
            if (currentIndex < flashcards.size) {
                displayCurrentQuestion()
                nextButton.isEnabled = false
            } else {
                showScoreScreen()
            }
        }
    }

    private fun displayCurrentQuestion() {
        findViewById<TextView>(R.id.questionTextView).text = flashcards[currentIndex].first
        findViewById<TextView>(R.id.feedbackTextView).text = ""
    }

    private fun recordAnswer(answer: Boolean) {
        userAnswers[currentIndex] = answer
        val isCorrect = (answer == flashcards[currentIndex].second)
        if (isCorrect) score++

        findViewById<TextView>(R.id.feedbackTextView).text =
            if (isCorrect) "✓ Correct" else "✗ Incorrect"
    }

    private fun showScoreScreen() {
        setContentView(R.layout.score_screen)

        findViewById<TextView>(R.id.scoreTextView).text = "$score/${flashcards.size}"
        findViewById<TextView>(R.id.feedbackTextView2).text = getPerformanceFeedback()

        findViewById<Button>(R.id.restartButton).setOnClickListener {
            resetQuiz()
            showQuestionScreen()
        }

        findViewById<Button>(R.id.reviewButton).setOnClickListener {
            showReviewScreen()
        }
        findViewById<Button>(R.id.exitButton).setOnClickListener(){
            finish()
    }
    }

    private fun getPerformanceFeedback(): String {
        return when {
            score == flashcards.size -> "Perfect! 🎯"
            score >= flashcards.size/2 -> "Good job! 👍"
            else -> "Keep practicing! 📚"
        }

        }


    private fun showReviewScreen() {
        setContentView(R.layout.review_screen)

        val reviewText = findViewById<TextView>(R.id.reviewTextView)
        reviewText.text = buildReviewText()

        findViewById<Button>(R.id.backButton).setOnClickListener {
            showScoreScreen()
        }
    }

    private fun buildReviewText(): String {
        val builder = StringBuilder()
        builder.append("=== QUIZ REVIEW ===\n\n")
        builder.append("Final Score: $score/${flashcards.size}\n\n")

        flashcards.forEachIndexed { index, (question, correctAnswer) ->
            val userAnswer = userAnswers[index]
            builder.append("Q${index + 1}: $question\n")
                .append("Your answer: ${formatAnswer(userAnswer)}\n")
                .append("Correct answer: ${if (correctAnswer) "True" else "False"}\n")
                .append("Result: ${if (userAnswer == correctAnswer) "✓ Correct" else "✗ Incorrect"}\n\n")
        }

        return builder.toString()
    }

    private fun formatAnswer(answer: Boolean?): String {
        return answer?.let { if (it) "True" else "False" } ?: "Not answered"
    }
}