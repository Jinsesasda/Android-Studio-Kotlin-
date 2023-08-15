package course.labs.graphicslab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import course.labs.graphicslab.databinding.ActivityBubbleBinding
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.internal.artificialFrame

class BubbleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBubbleBinding

    // Bubble image's bitmap
    private lateinit var mBitmap: Bitmap
    private lateinit var bubbleView: BubbleView
    private lateinit var scaledBitmap: Bitmap
    private lateinit var positionViewModel: BubblePositionViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBubbleBinding.inflate(layoutInflater)
        setContentView(binding.frame)

        setupUI()

        // ToDo:
        // 1. Get ViewModel (BubblePositionViewModel) using ViewModelProvider
        // 2. Tie BubblePositionViewModel to BubbleActivity lifecycle
        positionViewModel = ViewModelProvider(this).get(BubblePositionViewModel::class.java)
        positionViewModel.bindToActivityLifecycle(this)
    }

    private fun setupUI() {
        // Set up user interface

        // ToDo:
        // 1. Load basic bubble Bitmap using resources and R.drawable.shiny_steel_ball
        // 2. Scale bitmap to BITMAP_SIZE
        // 3. Create BubbleView. Position offscreen initially
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.shiny_steel_ball)
        scaledBitmap = Bitmap.createScaledBitmap(mBitmap, BITMAP_SIZE, BITMAP_SIZE, true)
        bubbleView = BubbleView(applicationContext,
            windowManager.currentWindowMetrics.bounds.width().toFloat(),
            windowManager.currentWindowMetrics.bounds.height().toFloat())
    }

    // Start animation here because you know the Activity
    // is visible on the screen
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            beginObservingCounts()
            with(binding.frame) {
                // ToDo:
                // Start animation motion here using the positionViewModel
                positionViewModel.startMotion(
                    windowManager.currentWindowMetrics.bounds.width(),
                    windowManager.currentWindowMetrics.bounds.height(),BITMAP_SIZE)
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

    private fun beginObservingCounts() {
        // Register observers of image position
        positionViewModel.location.observe(this) {
            // ToDo:
            // 1. Get the position from ViewModel
            // 2. Set the x and y attributes of the bubble view to the position values retrieved in the previous step
            // 3. invalidate bubble view
            bubbleView.x = positionViewModel.location.value?.first!!
            bubbleView.y = positionViewModel.location.value?.second!!
            bubbleView.postInvalidate()
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
