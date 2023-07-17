package kyrylost.apps.eatwise.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kyrylost.apps.eatwise.databinding.SignUpThirdFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel

class SignUpThirdFragment : Fragment() {
    private var _binding: SignUpThirdFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = SignUpThirdFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.thirdScreenFieldsSuccessfullySetted.observe(viewLifecycleOwner) {
            val navController =
                SignUpThirdFragmentDirections.actionSignUpThirdFragmentToSignUpFourthFragment()
            findNavController().navigate(navController)
        }

        userViewModel.thirdScreenFieldsSetError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        }

        binding.trainingsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress!=0 && progress!=8) {
                    val pos = progress * (seekBar.width - 2 * seekBar.thumbOffset) / seekBar.max
                    binding.progressTV.text = "$progress"
                    binding.progressTV.x = seekBar.x + pos + seekBar.thumbOffset / 2
                    binding.progressTV.visibility = View.VISIBLE
                }
                else binding.progressTV.visibility = View.INVISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.thirdSignUpContinue.setOnClickListener {
            val sexId = binding.sexRg.checkedRadioButtonId
            val workId = binding.workRg.checkedRadioButtonId
            val trainings = binding.trainingsSeekBar.progress

            userViewModel.setSexAndWorkAndTrainings(sexId, workId, trainings,
                binding.maleButton.id, binding.femaleButton.id,
                binding.activeButton.id, binding.sedentaryButton.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}