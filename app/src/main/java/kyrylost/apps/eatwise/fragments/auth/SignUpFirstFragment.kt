package kyrylost.apps.eatwise.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.databinding.SignUpFirstFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel

class SignUpFirstFragment : Fragment() {
    private var _binding: SignUpFirstFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFirstFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstSignUpContinue.setOnClickListener {
            val email = binding.emailAddressEt.text.toString()
            val password = binding.passwordEt.text.toString()

            userViewModel.setEmailAndPassword(email, password)
        }

        binding.signInTv.setOnClickListener {
            val navController =
                SignUpFirstFragmentDirections.actionSignUpFirstFragmentToSignInFragment()
            findNavController().navigate(navController)
        }

        subscribeToObservables()

    }

    private fun subscribeToObservables() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.firstScreenFieldsSuccessfullySetted.collect {
                    val navController =
                        SignUpFirstFragmentDirections.actionSignUpFirstFragmentToSignUpSecondFragment()
                    findNavController().navigate(navController)
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.firstScreenFieldsSetError.collect {
                    Toast.makeText(
                        requireContext(),
                        it,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}