package course.labs.networking

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import course.labs.networking.MainViewModel.Status.*
import course.labs.networking.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    // Binding to XML layout
    private lateinit var binding: MainFragmentBinding

    // Defining the viewModel
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the fragment binding and returning the binding root
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called by the framework once the view has been created.
     * This is the appropriate place to update the view or add
     * listeners before the view is actually displayed.
     **/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Register an observer to the viewModel's LiveData and respond to status updates.
        // 1. When the GET request is in progress, display the appropriate message.
        // 2. When the bitmap is downloaded, display the QR code.
        // 3. If an error is obtained, display the error message.
        // 4. After steps 2 and 3, re-enable the button that downloads the QR code.
        viewModel.statusLiveData.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Progress -> {
                    // TODO
                    // Display a message saying that the GET request/download is in progress.
                    binding.info.text = "Performing GET request"
                }

                is Result -> {
                    // Display the image and update the text view.
                    binding.info.text = null
                    binding.info.setCompoundDrawablesWithIntrinsicBounds(null,null,null,
                        BitmapDrawable(resources,status.image)
                    )

                    // TODO
                    // Re-enable button.
                    binding.button.isEnabled = true
                }

                is Error -> {
                    // TODO
                    // Display the error resource string and
                    // exception message in text view.
                    binding.info.text = "unable to load the QR code"

                    // TODO
                    // Re-enable button.
                    binding.button.isEnabled = true
                }
            }
        }


        /**
         * Set the button OnClickListener to call the network request
         * defined in the viewModel
         * **/
        binding.button.setOnClickListener {
            // TODO
            // Disable button.
            binding.button.isEnabled = false

            // TODO
            // Clear output text from the previous request.
            binding.info.text = null

            // TODO
            // Use viewModel to send asynchronous network request.
            viewModel.sendNetworkRequest()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}