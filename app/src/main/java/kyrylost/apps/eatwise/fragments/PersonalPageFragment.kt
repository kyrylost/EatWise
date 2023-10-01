package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.databinding.PersonalPageFragmentBinding
import kyrylost.apps.eatwise.viewmodel.UserViewModel

class PersonalPageFragment : Fragment() {
    private var _binding: PersonalPageFragmentBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PersonalPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userViewModel = userViewModel

        binding.trainingsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (seekBar.width == 0) return
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

        binding.trainingsSeekBar.doOnPreDraw {
            if (binding.trainingsSeekBar.progress != 0 && binding.trainingsSeekBar.progress != 8) {
                val pos = binding.trainingsSeekBar.progress * (binding.trainingsSeekBar.width - 2 *
                        binding.trainingsSeekBar.thumbOffset) / binding.trainingsSeekBar.max

                binding.progressTV.text = "${binding.trainingsSeekBar.progress}"
                binding.progressTV.x = binding.trainingsSeekBar.x + pos + binding.trainingsSeekBar.thumbOffset / 2
                binding.progressTV.visibility = View.VISIBLE
            }
        }

        binding.saveButton.setOnClickListener {
            userViewModel.updateUserData(
                binding.searchEt.text.toString(),
                binding.sexRg.getCheckedItemNumber(),
                binding.workRg.getCheckedItemNumber(),
                binding.trainingsSeekBar.progress,
                binding.dietRg.getCheckedItemNumber()
            )

            Toast.makeText(requireContext(), "Saving...", Toast.LENGTH_LONG).show()

        }

        subscribeToObservables()

    }

    private fun subscribeToObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.fieldUpdateInfo.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}