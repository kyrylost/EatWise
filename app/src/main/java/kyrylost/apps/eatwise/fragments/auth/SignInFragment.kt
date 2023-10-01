package kyrylost.apps.eatwise.fragments.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.databinding.SignInFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel

class SignInFragment : Fragment() {
    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var loggedInSharedPref: SharedPreferences
    private lateinit var emailSharedPref: SharedPreferences
    private lateinit var passwordSharedPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loggedInSharedPref = requireActivity().getSharedPreferences(
            "userLoggedInOnDevice", Context.MODE_PRIVATE
        ) ?: return
        emailSharedPref = requireActivity().getSharedPreferences(
            "email", Context.MODE_PRIVATE
        ) ?: return
        passwordSharedPref = requireActivity().getSharedPreferences(
            "password", Context.MODE_PRIVATE
        ) ?: return

        binding.signInBtn.setOnClickListener {
            val email = binding.emailAddressEt.text.toString()
            val password = binding.passwordEt.text.toString()

            userViewModel.setEmailAndPassword(email, password)
        }

        binding.signUpTv.setOnClickListener {
            val navController =
                SignInFragmentDirections.actionSignInFragmentToSignUpFirstFragment()
            findNavController().navigate(navController)
        }

        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.firstScreenFieldsSuccessfullySetted.collect {
                    userViewModel.login()
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userAuthAndCreatingSuccess.collect {
                    val navController =
                        SignInFragmentDirections.actionSignInFragmentToConsumedFragment()

                    emailSharedPref.edit().putString("email", userViewModel.getEmail()).apply()
                    passwordSharedPref.edit().putString("password", userViewModel.getPassword())
                        .apply()
                    loggedInSharedPref.edit().putBoolean("userIsLoggedIn", true).apply()

                    findNavController().navigate(navController)
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userAuthAndCreatingFailure.collect {
                    Toast.makeText(
                        requireContext(),
                        "You cannot be logged in right now",
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