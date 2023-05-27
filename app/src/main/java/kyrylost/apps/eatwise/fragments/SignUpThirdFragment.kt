package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kyrylost.apps.eatwise.databinding.SignUpThirdFragmentBinding

class SignUpThirdFragment : Fragment() {
    private var _binding: SignUpThirdFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = SignUpThirdFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}