package course.labs.graphicslab

import android.os.Looper
import android.os.Handler
import androidx.lifecycle.*

// This class manages the position and movement
// of the image. Publishes the current position as LiveData

class BubblePositionViewModel : ViewModel(), DefaultLifecycleObserver {

    // Current position
    private val _location: MutableLiveData<Pair<Float, Float>> =
        MutableLiveData<Pair<Float, Float>>()
    internal val location: LiveData<Pair<Float, Float>>
        get() = _location

    private var bitmapWidth: Int = 0 // Image size
    private var displayHeight: Int = 0 // display height
    private var displayWidth: Int = 0 // display width

    // Handler for running simulation
    private val handler: Handler = Handler(Looper.getMainLooper())

    // Image's trajectory in pixels per time step
    private var dx: Float = 10F
    private var dy: Float = 10F

    // Image's x,y coordinates
    private var xPos: Float = -1F
    private var yPos: Float = -1F

    // Simulation step time in ms
    private val rate = 60L

    // Runnable executed at each simulation step
    private lateinit var mover: Runnable

    // Bind ViewModel to lifecycle of BubbleActivity
    internal fun bindToActivityLifecycle(bubbleActivity: BubbleActivity) {
        bubbleActivity.lifecycle.addObserver(this)
    }

    // Stop simulation
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        handler.removeCallbacks(mover)
    }

    // Starts simulation
    // Caller passes in display dimensions and image width/height
    // ToDo:
    // Set first position in middle of the display
    // Create a Kotlin.Pair object holding x,y coordinates of
    // Bubble's position
    fun startMotion(x: Int, y: Int, size: Int) {
        displayWidth = x
        displayHeight = y
        bitmapWidth = size

        if (xPos < 0 || yPos < 0) {


            xPos = (displayWidth/2).toFloat()-(bitmapWidth/2).toFloat()
            yPos = (displayHeight/2).toFloat()-(bitmapWidth).toFloat()
            _location.value = Pair(xPos, yPos)

        }

        _location.value = Pair(xPos, yPos)

        // Runnable executed at each simulation step
        mover = Runnable {

            // Move position
            xPos += dx
            yPos += dy

            snapToEdge()

            deflect()

            // Post live data
            _location.value = Pair(xPos, yPos)

            // prepare for next update
            handler.postDelayed(mover, rate)
        }

        // First simulation step
        handler.post(mover)
    }

    private fun snapToEdge() {
        // ToDo:
        // If any part of the bubble image is offscreen
        // adjust the image position so it touches the screen

        if(xPos <= 0) {
            xPos = 0F
            _location.value = Pair(xPos, yPos)
        }
        if (yPos <= 0) {
            yPos = 0F
            _location.value = Pair(xPos, yPos)
        }
        if ((yPos) >= displayHeight.toFloat()){
            yPos = displayHeight.toFloat()
            _location.value = Pair(xPos, yPos)
        }

        else{
            xPos = displayWidth.toFloat() - (bitmapWidth).toFloat()
            _location.value = Pair(xPos, yPos)
        }
    }

    private fun deflect() {

        // ToDo:
        // If the bubble images intersects a particular display edge
        // change the sign of trajectory component to create bounce
        // effect
        if ((xPos + (bitmapWidth).toFloat()) >= displayWidth.toFloat()){
            dx *=-1
            xPos +=dx
            _location.value =Pair(xPos, yPos)
        }

        if ((yPos + 2*(bitmapWidth).toFloat()) >= displayHeight.toFloat()){
            dy *= -1
            yPos += dy
            _location.value =Pair(xPos, yPos)
        }
        if (xPos <= 0) {
            dx *=-1
            xPos +=dx
            _location.value =Pair(xPos, yPos)
        }
        else {
            dy *= -1
            yPos += dy
            _location.value = Pair(xPos, yPos)
        }
    }
}