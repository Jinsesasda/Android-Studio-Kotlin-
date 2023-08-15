package course.labs.permissionslab

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import course.labs.common.showSnackbar
import course.labs.permissionslab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            onButtonClick()
        }
    }

    private fun onButtonClick() {


        when {
            //TODO: If the permission has already been granted dial the number.
            // [Hint]: Use checkSelfPermission to check if the permission has been granted, and then call the dialPhoneNumber() method.

            checkSelfPermission(PERMISSION) == PackageManager.PERMISSION_GRANTED -> dialPhoneNumber()



            // Check if the user has previously denied permission and
            // therefore a rationale should be provided to help the user
            // understand the importance of granting this permission.
            shouldShowRequestPermissionRationale(PERMISSION) -> {
                binding.root.showSnackbar(
                    R.string.need_permission_string,
                    Snackbar.LENGTH_INDEFINITE,
                    android.R.string.ok
                ) {
                    requestPermissionLauncher.launch(PERMISSION)
                }
            }
            // User hasn't been asked for permission yet.
            else -> {
                // TODO: launch the permission request launcher.
                requestPermissionLauncher.launch(PERMISSION)
            }
        }
    }

    // Create the permission request launcher.
    // If permission granted, dial the phone number.
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            //TODO: Dial the phone number if the permission has been granted.
            if (isGranted) {
                dialPhoneNumber()
            } else {
                Toast.makeText(
                    this, getString(R.string.need_permission_string),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun dialPhoneNumber() {
        //TODO: Create an Intent with the appropriate action and data.
        // [Hint]:Implicitly start an Activity to call the desired Phone Number.
        val callIntent = Intent(ACTION_CODE)
        callIntent.data = Uri.parse("tel:" + PHONE_NUMBER_URI)
        startActivity(callIntent)
    }

    //TODO: Create a companion object that contains and defines
    // the ACTION string for dialing a phone number without user input,
    // the PERMISSION needed for doing this,
    // and PHONE_NUMBER_URI to be used in the Intent (the desired number is 5554).




    companion object{

        private const val ACTION_CODE = Intent.ACTION_CALL

        private const val PHONE_NUMBER_URI = "5554"
        private const val PERMISSION = "android.permission.CALL_PHONE"

    }
}