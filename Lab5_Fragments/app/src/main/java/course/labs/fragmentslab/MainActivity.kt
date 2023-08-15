package course.labs.fragmentslab

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import course.labs.fragmentslab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Create a private variable of type CounterViewModel to keep track of counts
    private lateinit var viewModel: CounterViewModel
    private lateinit var createView: TextView
    private lateinit var startView: TextView
    private lateinit var resumeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set content to the binding root view.
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)

        // TODO:
        viewModel = ViewModelProvider(this).get(CounterViewModel::class.java)
        // Initialize the CounterViewModel variable defined above
        // Bind CounterViewModel variable to the activity lifecycle

        viewModel.bindToActivityLifecycle(this)


    }
}