package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kyrylost.apps.eatwise.databinding.ConsumedFragmentBinding
import kyrylost.apps.eatwise.viewmodel.ConsumedNutrientsViewModel

class ConsumedFragment : Fragment() {
    private var _binding: ConsumedFragmentBinding? = null
    private val binding get() = _binding!!
    private val consumedNutrientsViewModel: ConsumedNutrientsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ConsumedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.consumedNutrientsViewModel = consumedNutrientsViewModel
        consumedNutrientsViewModel.calculateRecommendedNutrients()
        consumedNutrientsViewModel.getConsumedNutrients()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}