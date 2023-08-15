package course.labs.graphicslab

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*

// This class manages the position and movement
// of the image. Publishes the current position as LiveData

class BubblePositionViewModel : ViewModel(), DefaultLifecycleObserver, SensorEventListener {

    internal lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

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
    private var dx: Float = 0F
    private var dy: Float = 0F

    // Image's x,y coordinates
    private var xPos: Float = 0F
    private var yPos: Float = 0F

    // Simulation step time in ms
    private val rate = 60L

    // Runnable executed at each simulation step
    private lateinit var mover: Runnable

    // Bind ViewModel to lifecycle of BubbleActivity
    internal fun bindToActivityLifecycle(bubbleActivity: BubbleActivity) {
        bubbleActivity.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        //TODO: Get a reference to the device's accelerometer.
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //TODO: Register a listener for accelerometer updates, passing the following parameters:
        // 1. The listener
        // 2. The Sensor
        // 3. The SENSOR_DELAY_UI of the SensorManager.
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    // Stop simulation
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        //TODO: Unregister the listener for accelerometer updates.
        sensorManager.unregisterListener(this)

        handler.removeCallbacks(mover)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Not implemented
    }

    override fun onSensorChanged(event: SensorEvent) {
        //TODO: call the deflect() method with the appropriate parameters.
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val time = System.currentTimeMillis()
            if (time - value > UPDATE_THRESHOLD) {
                value = time
                val xValue = event.values[0]
                val yValue = event.values[1]
                deflect(xValue, yValue)
            }
        }
    }


    // Starts simulation
    // Caller passes in display dimensions and image width/height
    fun startMotion(x: Int, y: Int, size: Int) {
        displayWidth = x
        displayHeight = y
        bitmapWidth = size

        // If still in offscreen position
        // Set first position in middle of the display
        if (xPos < 0 || yPos < 0) {

            xPos = x / 2f
            yPos = y / 2f
        }

        // Create a Pair object holding x,y coordinates of
        // position
        _location.value = Pair(xPos, yPos)

        // Runnable executed at each simulation step
        mover = Runnable {

            // Move position
            xPos += dx
            yPos += dy

            snapToEdge()

            // Post live data
            _location.value = Pair(xPos, yPos)

            // prepare for next update
            handler.postDelayed(mover, rate)
        }

        // First simulation step
        handler.post(mover)
    }

    private fun snapToEdge() {
        // If position intersects a display edge
        // adjust position to directly touch the edge

        if (xPos <= 0.0) {
            xPos = 0.0F
        } else if (xPos + bitmapWidth >= displayWidth) {
            xPos = displayWidth - bitmapWidth.toFloat()
        }

        if (yPos <= 0.0) {
            yPos = 0.0F
        } else if (yPos + bitmapWidth >= displayHeight) {
            yPos = displayHeight - bitmapWidth.toFloat()
        }
    }

    private fun deflect(xForce: Float, yForce: Float) {
        dx = -xForce
        dy = yForce
    }
    private var value:Long = 0

    companion object {
        private const val UPDATE_THRESHOLD = 100
    }
}