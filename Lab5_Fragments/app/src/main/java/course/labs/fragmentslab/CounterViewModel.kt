package course.labs.fragmentslab

import android.util.Log
import androidx.lifecycle.*

class CounterViewModel : ViewModel(), DefaultLifecycleObserver {

    // This class defines the ViewModel which keeps track of the number of times onCreate(), onStart() and onResume() have been called.
    companion object {
        private const val TAG = "CounterViewModel"
    }

    // TODO :
    var countc = MutableLiveData<Int>()
    var countr = MutableLiveData<Int>()
    var counts = MutableLiveData<Int>()

    internal val creater: LiveData<Int>
        get() = countc
    internal val resumer: LiveData<Int>
        get() = countr
    internal val starter: LiveData<Int>
        get() = counts

    // Create variables to keep a track of the number of times onCreate(), onStart() and onResume() have been called.
    // To keep track of each count, define two variables as specified below.
    // Define a private variable of type MutableLiveData that can only be modified within the ViewModel class.
    // Define an internal/public variable of type LiveData that can be accessed externally by the UI/fragment but cannot be modified.
    // Use a backing property to specify the getter function for the internal/public variable
    // Refer to the link below for a more detailed explanation/example
    // https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel#4


    init {
        // TODO:
        countc.value = 0
        countr.value = 0
        counts.value = 0
        // Set initial value of counts to zero

    }

    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        // TODO :
        mainActivity.lifecycle.addObserver(this)
        // Add the current instance of CounterViewModel as a LifeCycleObserver to the MainActivity
        // Use the addObserver function

    }

    override fun onResume(owner: LifecycleOwner) {
        // TODO :
        countr.setValue(countr.value?.plus(1))
        // Update the appropriate count variable
        Log.i(TAG,"Entered onResume"+countr.value)

    }

    override fun onCreate(owner: LifecycleOwner) {
        // TODO :
        countc.setValue(countc.value?.plus(1))
        // Update the appropriate count variable
        Log.i(TAG,"Entered onCreate"+countc.value)

    }

    override fun onStart(owner: LifecycleOwner) {
        // TODO :
        counts.setValue(counts.value?.plus(1))        // Update the appropriate count variable
        Log.i(TAG,"Entered onStart"+counts.value)

    }


}