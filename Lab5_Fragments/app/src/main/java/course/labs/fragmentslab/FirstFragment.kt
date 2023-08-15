package course.labs.fragmentslab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import course.labs.fragmentslab.databinding.FirstFragmentBinding
import androidx.lifecycle.Observer
import course.labs.fragmentslab.databinding.SecondFragmentBinding

class FirstFragment : Fragment() {

    /** Binding to XML layout */
    private lateinit var binding: FirstFragmentBinding
    // Create a variable of type CounterViewModel to keep track of counts
    private lateinit var viewModel: CounterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided binding object to inflate the layout.
        binding = FirstFragmentBinding.inflate(inflater, container, false)

        // Update ActionBar label to distinguish which Fragment is displayed
        (requireActivity() as AppCompatActivity).supportActionBar?.title = this.javaClass.simpleName

        // Set onClickListener to navigate to the second fragmant from the first
        binding.fab.setOnClickListener {
            findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment()
            )
        }
        // Return the root view.
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO:
        // Initialize CounterViewModel instance
        viewModel = ViewModelProvider(requireActivity()).get(CounterViewModel::class.java)

        // TODO:
        // Use binding to display initial count

        binding = FirstFragmentBinding.bind(view)

        beginObservingCounter()
    }

    private fun beginObservingCounter() {
        // TODO:
        val co = Observer<Int> { binding.onCreate.text = "onCreate() calls: " + viewModel.creater.value}
        val ro = Observer<Int> { binding.onResume.text = "onResume() calls: " + viewModel.resumer.value }
        val so = Observer<Int> { binding.onStart.text = "onStart calls: " + viewModel.starter.value }
        // Register observer
        viewModel.creater.observe(viewLifecycleOwner, co)
        viewModel.resumer.observe(viewLifecycleOwner, ro)
        viewModel.starter.observe(viewLifecycleOwner, so)
        // Register observers for each of the count variables
        // In the body of the observe function, update the text to be displayed by using the binding

    }
}