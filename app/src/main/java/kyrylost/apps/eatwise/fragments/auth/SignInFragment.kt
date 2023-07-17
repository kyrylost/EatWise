package kyrylost.apps.eatwise.fragments.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kyrylost.apps.eatwise.databinding.SignInFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel

class SignInFragment : Fragment() {
    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.emailAndPasswordSuccessfullySetted.observe(viewLifecycleOwner) {
            userViewModel.login()
        }

        userViewModel.emailAndPasswordSetError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        }

        val loggedInSharedPref = requireActivity().getSharedPreferences(
            "userLoggedInOnDevice", Context.MODE_PRIVATE
        ) ?: return
        val emailSharedPref = requireActivity().getSharedPreferences(
            "email", Context.MODE_PRIVATE
        ) ?: return
        val passwordSharedPref = requireActivity().getSharedPreferences(
            "password", Context.MODE_PRIVATE
        ) ?: return

        userViewModel.userAuthAndCreatingSuccessMutableLiveData.observe(viewLifecycleOwner) {
            val navController = SignInFragmentDirections.actionSignInFragmentToConsumedFragment()

            emailSharedPref.edit().putString("email", userViewModel.getEmail()).apply()
            passwordSharedPref.edit().putString("password", userViewModel.getPassword()).apply()
            loggedInSharedPref.edit().putBoolean("userIsLoggedIn", true).apply()

            findNavController().navigate(navController)
        }
        userViewModel.userAuthAndCreatingFailureMutableLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "You cannot be logged in right now",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.signInBtn.setOnClickListener {
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