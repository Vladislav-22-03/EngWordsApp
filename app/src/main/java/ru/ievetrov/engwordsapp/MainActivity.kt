package ru.ievetrov.engwordsapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import ru.ievetrov.engwordsapp.databinding.ActivityLearnWordBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityLearnWordBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for ActivityLearnWordBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLearnWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trainer = LearnWordsTrainer()
        showNextQuestion(trainer)

        with(binding) {
            btnContinue.setOnClickListener {
                layoutResult.isVisible = false
                resetAnswerButtons()
                showNextQuestion(trainer)
            }

            btnSkip.setOnClickListener {
                showNextQuestion(trainer)
            }

            ibClose.setOnClickListener {
                val intent = Intent(this@MainActivity, HomePage::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showNextQuestion(trainer: LearnWordsTrainer) {
        val firstQuestion: Question? = trainer.getNextQuestion()
        with(binding) {
            if (firstQuestion == null || firstQuestion.variants.size < NUMBER_OF_ANSWERS) {
                tvQuestionWord.isVisible = false
                layoutVariants.isVisible = false
                btnSkip.text = "Complete"
            } else {
                btnSkip.isVisible = true
                tvQuestionWord.isVisible = true
                tvQuestionWord.text = firstQuestion.correctAnswer.original

                tvVariantValue1.text = firstQuestion.variants[0].translate
                tvVariantValue2.text = firstQuestion.variants[1].translate
                tvVariantValue3.text = firstQuestion.variants[2].translate
                tvVariantValue4.text = firstQuestion.variants[3].translate

                layoutAnswer1.setOnClickListener { handleAnswerClick(trainer, 0, layoutAnswer1, tvVariantNumber1, tvVariantValue1) }
                layoutAnswer2.setOnClickListener { handleAnswerClick(trainer, 1, layoutAnswer2, tvVariantNumber2, tvVariantValue2) }
                layoutAnswer3.setOnClickListener { handleAnswerClick(trainer, 2, layoutAnswer3, tvVariantNumber3, tvVariantValue3) }
                layoutAnswer4.setOnClickListener { handleAnswerClick(trainer, 3, layoutAnswer4, tvVariantNumber4, tvVariantValue4) }
            }
        }
    }

    private fun handleAnswerClick(
        trainer: LearnWordsTrainer,
        index: Int,
        layoutAnswer: LinearLayout,
        tvVariantNumber: TextView,
        tvVariantValue: TextView
    ) {
        val isCorrect = trainer.checkAnswer(index)
        if (isCorrect) {
            markAnswerCorrect(layoutAnswer, tvVariantNumber, tvVariantValue)
            showResultMessage(true)
        } else {
            markAnswerWrong(layoutAnswer, tvVariantNumber, tvVariantValue)
            showResultMessage(false)
            highlightCorrectAnswer(trainer) // Подсветка правильного ответа
        }

        disableAnswerButtons()
    }


    private fun disableAnswerButtons() {
        with(binding) {
            layoutAnswer1.isEnabled = false
            layoutAnswer2.isEnabled = false
            layoutAnswer3.isEnabled = false
            layoutAnswer4.isEnabled = false
        }
    }

    private fun resetAnswerButtons() {
        with(binding) {
            layoutAnswer1.isEnabled = true
            layoutAnswer2.isEnabled = true
            layoutAnswer3.isEnabled = true
            layoutAnswer4.isEnabled = true

            markAnswerNeutral(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
            markAnswerNeutral(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
            markAnswerNeutral(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
            markAnswerNeutral(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
        }
    }

    private fun markAnswerNeutral(layoutAnswer: LinearLayout, tvVariantNumber: TextView, tvVariantValue: TextView) {
        layoutAnswer.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_containers)
        tvVariantNumber.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_variants)
        tvVariantNumber.setTextColor(ContextCompat.getColor(this, R.color.textVariantsColor))
        tvVariantValue.setTextColor(ContextCompat.getColor(this, R.color.textVariantsColor))
    }

    private fun markAnswerWrong(layoutAnswer: LinearLayout, tvVariantNumber: TextView, tvVariantValue: TextView) {
        layoutAnswer.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_containers_wrong)
        tvVariantNumber.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_variants_wrong)
        tvVariantNumber.setTextColor(ContextCompat.getColor(this, R.color.white))
        tvVariantValue.setTextColor(ContextCompat.getColor(this, R.color.wrongAnswerColor))
    }

    private fun markAnswerCorrect(layoutAnswer: LinearLayout, tvVariantNumber: TextView, tvVariantValue: TextView) {
        layoutAnswer.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_containers_correct)
        tvVariantNumber.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_variants_correct)
        tvVariantNumber.setTextColor(ContextCompat.getColor(this, R.color.white))
        tvVariantValue.setTextColor(ContextCompat.getColor(this, R.color.correctAnswerColor))
    }

    private fun showResultMessage(isCorrect: Boolean) {
        val color: Int
        val messageText: String
        val resultIconResource: Int
        if (isCorrect) {
            color = ContextCompat.getColor(this, R.color.correctAnswerColor)
            resultIconResource = R.drawable.ic_correct
            messageText = "Correct!" // TODO: get from string resources
        } else {
            color = ContextCompat.getColor(this, R.color.wrongAnswerColor)
            resultIconResource = R.drawable.ic_wrong
            messageText = "Incorrect!" // TODO: get from string resources
        }

        with(binding) {
            btnSkip.isVisible = false
            layoutResult.isVisible = true
            btnContinue.setTextColor(color)
            layoutResult.setBackgroundColor(color)
            tvResultMessage.text = messageText
            ivResultIcon.setImageResource(resultIconResource)
        }
    }
    private fun highlightCorrectAnswer(trainer: LearnWordsTrainer) {
        with(binding) {
            if (trainer.checkAnswer(0)) markAnswerCorrect(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
            if (trainer.checkAnswer(1)) markAnswerCorrect(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
            if (trainer.checkAnswer(2)) markAnswerCorrect(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
            if (trainer.checkAnswer(3)) markAnswerCorrect(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
        }
    }

}
