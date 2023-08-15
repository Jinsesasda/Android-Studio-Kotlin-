package course.labs.activitylab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import course.labs.activitylab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // This lab is for understanding the lifecycle of Android Activity

    // Create counter variables for the number of times onCreate(), onRestart(), onStart() and
    // onResume() have been called
    // Increment these variables' values when their corresponding lifecycle methods get called
    // Update display of counts as appropriate

    // [Note]
    // Store and restore counter values to preserve values across reconfiguration and
    // starting/returning from SecondActivity

    private lateinit var binding: ActivityMainBinding

    // TODO:
    // Create four counter variables for onCreate(), onRestart(), onStart() and onResume()
    // Only need 4 lines of code
    private lateinit var createview: TextView
    private lateinit var restartview: TextView
    private lateinit var startview: TextView
    private lateinit var resumeview: TextView
    var CREATE = 0
    var RESTART = 0
    var ONSTART = 0
    var ONRESUME = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate() called")

        // Pass the root view to setContentView() to make it the active view on the screen
        // More details about view binding available at "https://developer.android.com/topic/libraries/view-binding"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This is a button starting the second activity
        val secondActivityLaunchBtn = binding.fab
        secondActivityLaunchBtn.setOnClickListener {
            // TODO :
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            // Launch the SecondActivity when this button is clicked
            // [Hint] Use startActivity() method with intent to start the SecondActivity
        }

        // If there's any saved previous state, we need to restore
        // Complete the function - restoreCounts
        savedInstanceState?.apply { restoreCounts(savedInstanceState) }
        createview = findViewById(R.id.on_create)
        restartview  = findViewById(R.id.on_restart)
        startview = findViewById(R.id.on_start)
        resumeview  = findViewById(R.id.on_resume)
        // TODO :
        CREATE += 1

        // Update the appropriate count variable
    }

    private fun displayCounts() {
        // Call this function when the screen needs to be updated
        // TODO
        // Complete the function - update TextViews to show the current
        // values of the variables of the counters
        // [Hint] Access TextViews using "binding" object like we did with "secondActivityLaunchBtn"
        // Only need 4 lines of code, one TextView for each counter variable
        createview.text = "onCreate() calls: $CREATE"
        restartview.text = "onRestart() calls: $RESTART"
        startview.text = "onStart() calls: $ONSTART"
        resumeview.text = "onResume() calls: $ONRESUME"
    }

    private fun restoreCounts (savedInstanceState:Bundle) {
        // TODO :
        CREATE = savedInstanceState.getInt(CREATE_KEY)
        RESTART = savedInstanceState.getInt(RESTART_KEY)
        ONSTART = savedInstanceState.getInt(START_KEY)
        ONRESUME = savedInstanceState.getInt(RESUME_KEY)
        // Only need 4 lines of code, restore four count variables using savedInstanceState
        // [Hint] You can get saved values from from savedInstanceState using "keys" defined
        // in the companion object.
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        // TODO :
        savedInstanceState.putInt(CREATE_KEY, CREATE)
        savedInstanceState.putInt(RESTART_KEY, RESTART)
        savedInstanceState.putInt(START_KEY, ONSTART)
        savedInstanceState.putInt(RESUME_KEY, ONRESUME)
        // Save counter information with a collection of key-value pairs
        // As keys, use const values defined in the companion object
        // Only 4 lines need for each counter variable
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart() called")
        // TODO :
        // Update the appropriate count variable
        ONSTART += 1
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume() called")
        // TODO :
        // 1. Update the appropriate count variable
        // 2. Update the user interface
        ONRESUME += 1

        displayCounts()

    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart() called")
        // TODO :
        // Update the appropriate count variable
        RESTART += 1
    }

    companion object {
        // Use these values as map keys for storing/retrieving respective counter values
        private const val RESTART_KEY = "restart"
        private const val RESUME_KEY = "resume"
        private const val START_KEY = "start"
        private const val CREATE_KEY = "create"
        private const val TAG = "Lab-Activity"
    }
}