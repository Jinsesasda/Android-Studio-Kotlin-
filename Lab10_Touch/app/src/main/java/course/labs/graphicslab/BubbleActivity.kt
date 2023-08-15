package course.labs.graphicslab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import course.labs.graphicslab.databinding.ActivityBubbleBinding

class BubbleActivity : AppCompatActivity() {

    // The goal of this lab is to understand how to create a gesture detector and how to attach it
    // to specific views within your app
    private lateinit var binding: ActivityBubbleBinding

    // Bubble image's bitmap
    private lateinit var mBitmap: Bitmap
    private lateinit var bubbleView: BubbleView
    private lateinit var scaledBitmap: Bitmap
    private lateinit var positionViewModel: BubblePositionViewModel
    private lateinit var gestureDetector: GestureDetector

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBubbleBinding.inflate(layoutInflater)
        setContentView(binding.frame)

        setupUI()
        initGestureDetector()

        // ToDo:
        // 1. Get ViewModel (BubblePositionViewModel) using ViewModelProvider
        positionViewModel = ViewModelProvider(this).get(BubblePositionViewModel::class.java)
        // 2. Tie BubblePositionViewModel to BubbleActivity lifecycle
        positionViewModel.bindToActivityLifecycle(this)


    }

    private fun setupUI() {
        // Set up user interface

        // ToDo:
        // 1. Load basic bubble Bitmap using resources and R.drawable.shiny_steel_ball
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.shiny_steel_ball)
        // 2. Scale bitmap to BITMAP_SIZE
        scaledBitmap = Bitmap.createScaledBitmap(mBitmap, BITMAP_SIZE, BITMAP_SIZE, false)
        // 3. Create BubbleView. Position offscreen initially
        bubbleView = BubbleView(this, -1F, -1F)

    }

    // Init GestureDetector to detect onFling event
    private fun initGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            // If a fling gesture starts on a BubbleView then change the BubbleView's trajectory
            override fun onFling(
                event1: MotionEvent,
                event2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                Log.i(TAG, "onFling detected")
                // ToDo:
                // Call the function - "deflect" in positionViewModel when onFling detected
                positionViewModel.deflect(velocityX, velocityY, BITMAP_SIZE)
                return true
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                return true
            }

            // Good practice to override this method because all gestures start with a ACTION_DOWN event
            override fun onDown(event: MotionEvent): Boolean {
                return true
            }
        })
    }

    // Start animation here because you know the Activity
    // is visible on the screen
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            beginObservingBubbleLocation()
            with(binding.frame) {
                // ToDo:
                // Start animation motion here using the positionViewModel
                positionViewModel.startMotion(
                    resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels,
                    BITMAP_SIZE)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // ToDo:
        // Add BubbleView to FrameLayout
        binding.frame.addView(bubbleView)
    }

    override fun onStop() {
        // ToDo:
        // Remove BubbleView from FrameLayout
        binding.frame.removeView(bubbleView)
        super.onStop()
    }

    private fun beginObservingBubbleLocation() {
        // Register observers of image position

        // ToDo:
        // 1. Get the position from ViewModel
        // 2. Set the x and y attributes of the bubble view to the position values retrieved in the previous step
        // 3. invalidate bubble view
        positionViewModel.location.observe(this){

            bubbleView.y = it?.second?: -1F
            bubbleView.x = it?.first?: -1F
            bubbleView.postInvalidate()
        }
    }

    // BubbleView is a View that displays a bubble.
    // This class handles drawing and animating the BubbleView
    inner class BubbleView internal constructor(
        context: Context,
        x: Float,
        y: Float
    ) : View(context) {
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

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            // ToDo:
            // Replace return false statement with a right code
            // Return gestureDetector's onTouchEvent with passing "event" param to detect touch event
            //Returning gesture
            return gestureDetector.onTouchEvent(event)
        }
    }

    companion object {
        private const val TAG = "TouchMasterLab"
        private const val BITMAP_SIZE = 250
    }
}