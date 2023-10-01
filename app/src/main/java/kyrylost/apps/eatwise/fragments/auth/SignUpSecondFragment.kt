package kyrylost.apps.eatwise.fragments.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.databinding.SignUpSecondFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel

class SignUpSecondFragment : Fragment() {
    private var _binding: SignUpSecondFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpSecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.secondSignUpContinue.setOnClickListener {
            val birthday = binding.dateEt.text.toString()
            val weight = binding.weightEt.text.toString()

            userViewModel.setBirthdayAndWeight(birthday, weight)
        }

        binding.dateEt.addTextChangedListener(object : TextWatcher {

            private var isFormatting: Boolean = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (isFormatting) {
                    return
                }

                isFormatting = true

                // Remove any existing slash
                val original = s.toString().replace("/", "")

                val formatted = StringBuilder()
                var index = 0

                // Add slashes in the desired positions
                if (original.length >= 2) {
                    formatted.append(original.substring(0, 2))
                    formatted.append("/")
                    index += 2
                }

                if (original.length >= 4) {
                    formatted.append(original.substring(2, 4))
                    formatted.append("/")
                    index += 2
                }

                // Add remaining characters
                if (original.length > index) {
                    formatted.append(original.substring(index))
                }

                binding.dateEt.setText(formatted.toString())

                if (formatted.isNotEmpty()) {
                    if (formatted.last() == '/') {
                        binding.dateEt.setSelection(formatted.length - 1)
                    }
                    else {
                        binding.dateEt.setSelection(formatted.length)
                    }
                }

                isFormatting = false
            }
        })

        subscribeToObservables()

    }

    private fun subscribeToObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.secondScreenFieldsSuccessfullySetted.collectLatest {
                    Log.d("secondScreenFieldsSuccessfullySetted", "SecondScr")
                    val navController =
                        SignUpSecondFragmentDirections.actionSignUpSecondFragmentToSignUpThirdFragment()
                    findNavController().navigate(navController)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.secondScreenFieldsSetError.collectLatest {
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