package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kyrylost.apps.eatwise.databinding.PersonalPageFragmentBinding

class PersonalPageFragment : Fragment() {
    private var _binding: PersonalPageFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = PersonalPageFragmentBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}