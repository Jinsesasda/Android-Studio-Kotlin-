package course.labs.activitylab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import course.labs.activitylab.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

}