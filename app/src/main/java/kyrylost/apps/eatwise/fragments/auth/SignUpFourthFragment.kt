package kyrylost.apps.eatwise.fragments.auth

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kyrylost.apps.eatwise.R
import kyrylost.apps.eatwise.databinding.SignUpFourthFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel


class SignUpFourthFragment : Fragment() {
    private var _binding: SignUpFourthFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFourthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.fourthScreenFieldsSetError.observe(viewLifecycleOwner) {
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
            val navController =
                SignUpFourthFragmentDirections.actionSignUpFourthFragmentToConsumedFragment()

            emailSharedPref.edit().putString("email", userViewModel.getEmail()).apply()
            passwordSharedPref.edit().putString("password", userViewModel.getPassword()).apply()
            loggedInSharedPref.edit().putBoolean("userIsLoggedIn", true).apply()

            findNavController().navigate(navController)
        }

        userViewModel.userAuthAndCreatingFailureMutableLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "Account cannot be created now!",
                Toast.LENGTH_LONG
            ).show()
        }


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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}