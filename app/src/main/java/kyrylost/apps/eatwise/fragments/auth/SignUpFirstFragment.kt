package kyrylost.apps.eatwise.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

        userViewModel.emailAndPasswordSuccessfullySetted.observe(viewLifecycleOwner) {
            val navController =
                SignUpFirstFragmentDirections.actionSignUpFirstFragmentToSignUpSecondFragment()
            findNavController().navigate(navController)
        }

        userViewModel.emailAndPasswordSetError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        }

        binding.firstSignUpContinue.setOnClickListener {
            val email = binding.emailAddressEt.text.toString()
            val password = binding.passwordEt.text.toString()

            userViewModel.setEmailAndPassword(email, password)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}