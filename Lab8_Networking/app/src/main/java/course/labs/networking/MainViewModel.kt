package course.labs.networking

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.*
import java.net.URL

class MainViewModel : ViewModel() {

    // Defining a pair of LiveData and MutableLiveData variables of type Status
    // The Status variable will be updated to display the downloaded bitmap, the status of the network request and
    // error messages if any
    private val _statusLiveData = MutableLiveData<Status>()
    val statusLiveData: LiveData<Status>
        get() = _statusLiveData


    /** Keep track of coroutine so that duplicate requests are ignored. */
    private var job: Job? = null

    /**
     * Defining a sealed class to store the status of the network request,
     * the downloaded bitmap and the error message, if any
     */
    sealed class Status {
        data class Progress(val progress: Int) : Status()
        data class Result(val image: Bitmap) : Status()
        data class Error(val errorResId: Int, val e: Exception) : Status()
    }

    /**
     * sendNetworkRequest() is a non-blocking function called by the UI (Fragment) when the
     * user clicks the send button. This function starts a coroutine
     * that sends a network request and then posts downloaded bitmap
     * on the LiveData feed that can be observed by the calling
     * Fragment.
     */
    fun sendNetworkRequest() {
        // TODO:
        // Ignore button click if a request is still active.
        if (job?.isActive == true) {
            return
        }

        // TODO:
        // Update the Progress field of the Status LiveData feed and say that the GET request is being performed
        _statusLiveData.value = Status.Progress(R.string.get_request_progress)
        _statusLiveData.postValue(Status.Progress(R.string.get_request_progress))

        // Launch a new coroutine to run network request in the background.
        job = viewModelScope.launch {
            try {
                // TODO:
                // Run the suspending network request to download the bitmap and store it in a variable
                val bit = makeNetworkCall(URL)

                // TODO:
                // Post the downloaded bitmap to the Result field of the Status LiveData Feed
                _statusLiveData.postValue(Status.Result(bit))

            } catch (e: Exception) {
                // TODO:
                // Something went wrong ... post an error message to the Error field of the Status LiveData feed.
                _statusLiveData.postValue(Status.Error(R.string.send_request_error, e))
            }
        }
    }

    /**
     * makeNetworkCall() is a suspending helper function that performs the network request
     * specified by the passed [url] and returns a Bitmap.
     */
    private suspend fun makeNetworkCall(url: String): Bitmap =
        withContext(Dispatchers.IO) {

            // For testing purposes, simulate network delay
            delay(2000)

            // TODO:
            // Construct a new Ktor HttpClient to perform the GET
            // request and then return the resulting Bitmap
            // Use the decodeStream function from BitmapFactory to decode the HttpClient output into a Bitmap
            // Resize the Bitmap to dimension (SIZE, SIZE) using the createScaledBitmap function
            //val client = HttpClient().get<String>(url)
            val Stream = URL(url).openStream()


            val map = BitmapFactory.decodeStream(Stream)
            // Replace the line below with the code to download the bitmap
            // The line below has only been included so that the initial app compiles without errors
            //Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.RGB_565)

            Bitmap.createScaledBitmap(map, SIZE, SIZE, true)
        }

    /**
     * Constants
     * Use these constants in the makeNetworkCall() function
     * Download a square image of dimension (SIZE, SIZE) specified in URL below
     */
    companion object {
        private const val URL =
            "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://www.cs.umd.edu/class/fall2022/cmsc436/&choe=UTF-8"
        //"https://image-charts.com/chart?chs=150x150&cht=qr&chl=https://www.cs.umd.edu/class/fall2022/cmsc436/&choe=UTF-8"
        private const val SIZE = 450
    }
}