package course.labs.graphicslab

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.view.ViewConfiguration
import androidx.lifecycle.*
import kotlin.math.abs

// This class manages the position and movement of the bubble image and publishes the current
// position as LiveData.
// [Note]
// "application" parameter is used to get access to system context - doesn't have to be passed in
// by owner of this ViewModel
class BubblePositionViewModel(
    application: Application
) : AndroidViewModel(application), DefaultLifecycleObserver {

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

    // Set max fling velocity
    private val maxFlingVelocity: Int =
        ViewConfiguration.get(application).scaledMaximumFlingVelocity

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
    // Caller passes in display dimensions and image size
    fun startMotion(x: Int, y: Int, size: Int) {
        displayWidth = x
        displayHeight = y
        bitmapWidth = size

        // If still in offscreen position
        // Set first position in middle of the display
        if (xPos < 0 || yPos < 0) {
            // ToDo:
            // Set first position in middle of the display
            xPos = (displayWidth/2).toFloat()-(bitmapWidth/2).toFloat()
            yPos = (displayHeight/2).toFloat()-(bitmapWidth/2).toFloat()
            _location.value = Pair(xPos, yPos)
        }

        // Create a Kotlin.Pair object holding x,y coordinates of
        // Bubble's position
        _location.value = Pair(xPos, yPos)

        // Runnable executed at each simulation step
        mover = Runnable {
            // ToDo:
            // Apply decay
            // Two lines of code needed
            // - If the absolute value of dx is greater than or equal to 0.0, multiply DECAY value to dx
            // - If the absolute value of dy is greater than or equal to 0.0, multiply DECAY value to dy
            if (abs(dx) >= 0.0) {
                dx *= DECAY
            }
            if (abs(dy) >= 0.0) {
                dy *= DECAY
            }

            // ToDo:
            // Move bubble position
            // Two lines of code needed
            // - Update xPos: Add up dx to the current xPos
            // - Update yPos: Add up dy to the current yPos
            xPos += dx
            yPos += dy

            // Adjust bubble position
            snapToEdgeAndChangeTrajectory()

            // Post live data
            _location.value = Pair(xPos, yPos)

            // Prepare for next update
            handler.postDelayed(mover, rate)
        }

        // First simulation step
        handler.post(mover)
    }

    // If position intersects a display edge, move position to directly touch edge
    // change dx, dy to mimic rebounding off the edge
    private fun snapToEdgeAndChangeTrajectory() {
        // 1. Adjust x position and dx of the bubble
        if (xPos <= 0.0) {
            // ToDo:
            // Adjust xPos and dx
            dx *= -1
            xPos = 0F
//            _location.value = Pair(xPos, yPos)
        } else if (bitmapWidth +xPos >= displayWidth) {
            // ToDo:
            // Adjust xPos and dx
            dx *= -1
            xPos = displayWidth.toFloat() - (bitmapWidth).toFloat()
//            _location.value = Pair(xPos, yPos)
        }

        // 2. Adjust y position and dy of the bubble
        if (yPos <= 0.0) {
            // ToDo:
            // Adjust yPos and dy
            dy *= -1
            yPos = 0F
//            _location.value = Pair(xPos, yPos)
        } else if (yPos + bitmapWidth >= displayHeight) {
            // ToDo:
            // Adjust yPos and dy
            dy *= -1
            yPos = displayHeight.toFloat() - bitmapWidth.toFloat()
//            _location.value = Pair(xPos, yPos)
        }
    }

    // Scales the fling forces to values proportional to the size of the bitmap and then uses the
    // scaled values to reset dx and dy
    fun deflect(xVelocity: Float, yVelocity: Float, bitmapSize: Int) {
        val maxScaled = bitmapSize * 0.9F
        val minScaled = -maxScaled

        dx = (2 * maxScaled) * ((xVelocity + maxFlingVelocity) / (2 * maxFlingVelocity)) + minScaled
        dy = (2 * maxScaled) * ((yVelocity + maxFlingVelocity) / (2 * maxFlingVelocity)) + minScaled
    }

    companion object {
        private const val DECAY = 0.985F // Slows image velocity over time
    }
}