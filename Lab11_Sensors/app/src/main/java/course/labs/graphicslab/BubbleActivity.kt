package course.labs.graphicslab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import course.labs.graphicslab.databinding.ActivityBubbleBinding

class BubbleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBubbleBinding

    // Bubble image's bitmap
    private lateinit var mBitmap: Bitmap
    private lateinit var bubbleView: BubbleView
    private lateinit var scaledBitmap: Bitmap
    private lateinit var positionViewModel: BubblePositionViewModel
    private lateinit var sensorManager: SensorManager

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBubbleBinding.inflate(layoutInflater)
        setContentView(binding.frame)

        setupUI()

        // Get SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Get ViewModel
        positionViewModel = ViewModelProvider(this)[BubblePositionViewModel::class.java]

        // Tie TickerViewModel to TickerDisplayActivity lifecycle
        positionViewModel.bindToActivityLifecycle(this)

        //TODO: Set the ViewModel's sensorManager property.
        positionViewModel.sensorManager = sensorManager

    }

    private fun setupUI() {
        // Set up user interface

        // Load basic bubble Bitmap
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.shiny_steel_ball)

        // Scale bitmap to BITMAP_SIZE
        scaledBitmap = Bitmap.createScaledBitmap(
            mBitmap, BITMAP_SIZE, BITMAP_SIZE, false
        )

        // Create BubbleView. Position offscreen initially
        bubbleView = BubbleView(
            applicationContext,
            -BITMAP_SIZE.toFloat(),
            -BITMAP_SIZE.toFloat()
        )
    }

    // Start animation here because you know the Activity
    // is visible on the screen
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            beginObservingCounts()

            with(binding.frame) {
                // Start animation motion here using the positionViewModel
                positionViewModel.startMotion(width, height, BITMAP_SIZE)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Add BubbleView to FrameLayout
        binding.frame.addView(bubbleView)
    }

    override fun onStop() {
        //Remove BubbleView from FrameLayout
        binding.frame.removeView(bubbleView)
        super.onStop()
    }

    private fun beginObservingCounts() {
        // Register observers of image position

        // 1. Get the position from ViewModel
        // 2. Set the x and y attributes of the bubble view to the position values retrieved in the previous step
        // 3. invalidate bubble view
        positionViewModel.location.observe(this) {
            bubbleView.x = it.first
            bubbleView.y = it.second
            bubbleView.invalidate()
        }
    }

    // BubbleView is a View that displays a bubble.
    // This class handles drawing and animating the BubbleView
    inner class BubbleView internal constructor(context: Context, x: Float, y: Float) :
        View(context) {
        private val painter = Paint()

        init {
            // Creates the bubble bitmap for this BubbleView
            this.x = x
            this.y = y
            painter.isAntiAlias = true
        }

        // Draw the Bubble at its current location
        override fun onDraw(canvas: Canvas) {
            canvas.drawBitmap(scaledBitmap, 0f, 0f, painter)
        }
    }

    companion object {
        private const val BITMAP_SIZE = 250
    }
}