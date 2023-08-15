package course.labs.networking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            // TODO
            // Initialize MainFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance()).commitNow()
        }
    }
}