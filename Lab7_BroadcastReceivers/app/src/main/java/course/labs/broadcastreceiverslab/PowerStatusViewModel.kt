package course.labs.broadcastreceiverslab

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PowerStatusViewModel : ViewModel(), DefaultLifecycleObserver {

    // Current connection status
    var isPowerConnected: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

}