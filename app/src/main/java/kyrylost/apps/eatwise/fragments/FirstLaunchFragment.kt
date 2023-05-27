package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kyrylost.apps.eatwise.R
import kyrylost.apps.eatwise.databinding.FirstLaunchFragmentBinding

class FirstLaunchFragment : Fragment(R.layout.first_launch_fragment) {
    private var _binding: FirstLaunchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstLaunchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            val action = FirstLaunchFragmentDirections
                .actionFirstLaunchFragmentToSignInFragment()
            findNavController().navigate(action)
        }

        binding.createAccountButton.setOnClickListener {
            val action = FirstLaunchFragmentDirections
                .actionFirstLaunchFragmentToSignUpFirstFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}