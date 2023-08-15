package course.labs.broadcastreceiverslab

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import course.labs.broadcastreceiverslab.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    // This lab is for understanding BroadcastReceivers that responds to device status in runtime

    // Update the period of pulsing image according to the device's charging status
    // We need two main objects - 1)BroadcastReceiver and 2)ViewModel

    // 1)BroadcastReceiver - Responds to the device's charging status
    private lateinit var receiver: PowerConnectionStatusReceiver

    // 2)ViewModel - Manages the pulsing period of the main image
    // Get reference to CounterViewModel using viewModels delegate
    private lateinit var viewModel: PowerStatusViewModel

    // Binding to XML layout
    private lateinit var binding: MainActivityBinding

    // Handler for periodically pulsing image. Will cover in a later lecture.
    // More details about Handler available at
    // "https://developer.android.com/reference/android/os/Handler"
    private lateinit var handler: Handler

    // Pulsing period of the main image
    private var pulsingRate = 0L

    // Runnable for toggling image visibility
    private val imageUpdater: Runnable = object : Runnable {
        override fun run() {
            // Update the image visibility for the pulsing effect
            binding.image.visibility = if (binding.image.isVisible) View.INVISIBLE
            else View.VISIBLE
            // This will be repeated at regular interval which is defined with the "pulsingRate" parameter
            handler.postDelayed(this, pulsingRate)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pass the root view to setContentView() to make it the active view on the screen
        // More details about view binding available at
        // "https://developer.android.com/topic/libraries/view-binding"
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create handler to update the image visibility
        handler = Handler(Looper.getMainLooper())

        // Initialize viewModel
        viewModel = ViewModelProvider(this)[PowerStatusViewModel::class.java]

        // TODO :
        // Create the receiver passing viewModel as a constructor parameter
        receiver = PowerConnectionStatusReceiver(viewModel)

        // Observe changes to powerStatus
        observeViewModelPowerStatus()
    }

    private fun observeViewModelPowerStatus() {
        // Register observer of powerStatus
        viewModel.isPowerConnected.observe(this) {
            // TODO :
            // The pulsing rate should be updated according to the viewModel value (isPowerConnected)
            // [Hint] Use constant values - HIGH_SPEED, LOW_SPEED
            var value = viewModel.isPowerConnected.getValue()==true

            when (value) {
                true -> pulsingRate = HIGH_SPEED
                else-> {
                    pulsingRate = LOW_SPEED
                }
            }





            // Handler would toggle the image visibility with the changed pulsing rate value
            handler.run {
                removeCallbacks(imageUpdater)
                postDelayed(imageUpdater, pulsingRate)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart() called")
        // TODO :
        // Register receiver dynamically using IntentFilter
        // 2 lines of codes need - Intent.ACTION_POWER_CONNECTED & Intent.ACTION_POWER_DISCONNECTED
        val Intentchange = IntentFilter()
        Intentchange.addAction(Intent.ACTION_POWER_DISCONNECTED)
        Intentchange.addAction(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(receiver, Intentchange)

    }

    // Update user interface by tracking power connection status
    // [Note]
    // Keep in mind that the screen needs to be updated when a user reenters the app
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume() called")
        // TODO :
        // Get the current power connection status from the system
        // Then update the value of viewModel's connection status
        // [Hint] How to get connection status from Intent
        // https://developer.android.com/training/monitoring-device-state/battery-monitoring
        val battery: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            registerReceiver(null, ifilter)
        }
        val Plug: Int = battery?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        val ACcharger: Boolean = Plug == BatteryManager.BATTERY_PLUGGED_AC
        if (ACcharger == false){

            viewModel.isPowerConnected.setValue(false)
        }
        else{
            viewModel.isPowerConnected.setValue(true)
        }
        observeViewModelPowerStatus()


    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause() called")
        // Stop the handler toggling image visibility
        handler.removeCallbacks(imageUpdater)
    }

    override fun onStop() {
        Log.i(TAG, "onStop() called")
        // TODO :
        // Unregister receiver dynamically
        unregisterReceiver(receiver)

        super.onStop()
    }

    companion object {
        // Constant values for pulsing period
        // HIGH_SPEED : Update the image visibility every 250ms
        // LOW_SPEED : Update the image visibility every 1000ms(1s)
        const val HIGH_SPEED = 250L
        const val LOW_SPEED = 1000L

        const val TAG = "Lab_BR"
    }
}