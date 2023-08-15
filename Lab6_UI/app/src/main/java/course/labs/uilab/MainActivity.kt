package course.labs.uilab

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import course.labs.uilab.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    // This lab is for understanding how to use and update UI elements of an Android App
    // The color in the black box and the color hex code should update based on the changes made to the 4 bars: alpha, red, green and blue

    private lateinit var binding: ActivityMainBinding
    // TODO :
    // Create 4 Int variables to store the value of the alpha, red, green and blue progress bar
    private var alpha: Int = 0
    private var red: Int = 0
    private var green: Int = 0
    private var blue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
    }

    private fun setUpUI() {
        // TODO :
        alpha = 255
        red = 0
        green = 0
        blue = 0

        binding.alphaBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    // TODO:
                    alpha = p1
                    updateColorDisplay()
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

        binding.redBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    // TODO:
                    red = p1
                    updateColorDisplay()
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

        binding.greenBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    // TODO:
                    green = p1
                    updateColorDisplay()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

        binding.blueBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    // TODO:
                    blue = p1
                    updateColorDisplay()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

        // TODO:
        updateColorDisplay()

    }

    private fun updateColorDisplay() {

        // TODO:
        binding.currColor.setText("#"+toHex(alpha)+toHex(red)+toHex(green)+toHex(blue))
        // TODO:
        binding.colorView.setBackgroundColor(Color.argb(alpha, red, green, blue))
    }


    private fun toHex(value: Int): String {
        val color = Integer.toHexString(value)
        return if (color.length <= 1) ("0$color").uppercase(Locale.ROOT)
        else color.uppercase(Locale.ROOT)
    }
}