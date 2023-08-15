package course.labs.broadcastreceiverslab

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.PowerManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

class PowerConnectionStatusReceiver(
    private val viewModel: PowerStatusViewModel
) : BroadcastReceiver() {
    // This class is for receiving messages of the device's power connection status from the Android system
    override fun onReceive(context: Context?, intent: Intent?) {
        // TODO :
        // Update the viewModel's connection status value according using Intent action
        val connection = (intent?.action.equals(Intent.ACTION_POWER_CONNECTED))
        //connecting with Action_power_connection
        viewModel.isPowerConnected.setValue(connection)
    }
}