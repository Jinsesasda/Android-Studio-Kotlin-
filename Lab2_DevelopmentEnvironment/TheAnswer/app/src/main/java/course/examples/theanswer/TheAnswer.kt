package course.examples.theanswer

import android.content.ContentValues.TAG
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import course.examples.theanswer.databinding.AnswerLayoutBinding
import android.util.Log

class TheAnswer : AppCompatActivity() {

    companion object {
        private val answers = intArrayOf(42, -10, 0, 100, 1000)
        private const val answer = 42
        private const val TAG = "TheAnswer"


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // Required call through to Activity.onCreate()‹
        // Restore any saved instance state
        super.onCreate(savedInstanceState)

        // Set up the application's user interface (content view)
        val binding = AnswerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG,"printing the answer to life");
        val value = findAnswer()
        val output =
                if (value == answer) answer.toString() else getString(R.string.never_know_string)

        // Get a reference to a TextView in the content view
        // Set desired text in answerView TextView
        binding.answerView.text = output
    }

    private fun findAnswer(): Int? {
        // Incorrect behavior
         return answers.firstOrNull { it == answer }
        // Correct behavior
         // return answers.firstOrNull { it == answer }
    }
}