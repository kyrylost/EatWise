package kyrylost.apps.eatwise.fragments.auth

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.R
import kyrylost.apps.eatwise.databinding.SignUpFourthFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel


class SignUpFourthFragment : Fragment() {
    private var _binding: SignUpFourthFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var loggedInSharedPref: SharedPreferences
    private lateinit var emailSharedPref: SharedPreferences
    private lateinit var passwordSharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFourthFragmentBinding.inflate(inflater, container, false)
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

        var diet = ""

        // Create a rounded rectangle shape with borders
        val borderDrawable = GradientDrawable()
        borderDrawable.shape = GradientDrawable.RECTANGLE
        borderDrawable.cornerRadius = 40f

        borderDrawable.setStroke(
            2,
            ContextCompat.getColor(requireContext(), R.color.white)
        )

        binding.weightMaintenanceLayout.setOnClickListener {
            diet = "maintenance"
            binding.weightMaintenanceLayout.background = borderDrawable
            binding.weightGainLayout.background = null
            binding.weightLossLayout.background = null
            binding.cuttingDietLayout.background = null
        }

        binding.weightGainLayout.setOnClickListener {
            diet = "gaining"
            binding.weightMaintenanceLayout.background = null
            binding.weightGainLayout.background = borderDrawable
            binding.weightLossLayout.background = null
            binding.cuttingDietLayout.background = null
        }

        binding.weightLossLayout.setOnClickListener {
            diet = "losing"
            binding.weightMaintenanceLayout.background = null
            binding.weightGainLayout.background = null
            binding.weightLossLayout.background = borderDrawable
            binding.cuttingDietLayout.background = null
        }

        binding.cuttingDietLayout.setOnClickListener {
            diet = "cutting"
            binding.weightMaintenanceLayout.background = null
            binding.weightGainLayout.background = null
            binding.weightLossLayout.background = null
            binding.cuttingDietLayout.background = borderDrawable
        }

        binding.fourthSignUpFinish.setOnClickListener {
            userViewModel.setDiet(diet)
        }

        subscribeToObservables()

    }

    private fun subscribeToObservables() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.fourthScreenFieldsSetError.collect() {
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
                        SignUpFourthFragmentDirections.actionSignUpFourthFragmentToConsumedFragment()

                    emailSharedPref.edit().putString("email", userViewModel.getEmail()).apply()
                    passwordSharedPref.edit().putString("password", userViewModel.getPassword()).apply()
                    loggedInSharedPref.edit().putBoolean("userIsLoggedIn", true).apply()

                    findNavController().navigate(navController)
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userAuthAndCreatingFailure.collect() {
                    Toast.makeText(
                        requireContext(),
                        "Account cannot be created now!",
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