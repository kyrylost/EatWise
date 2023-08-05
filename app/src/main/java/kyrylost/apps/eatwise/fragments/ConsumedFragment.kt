package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kyrylost.apps.eatwise.fragments.dialogs.NutrientDialogFragment
import kyrylost.apps.eatwise.R
import kyrylost.apps.eatwise.databinding.ConsumedFragmentBinding
import kyrylost.apps.eatwise.fragments.dialogs.YesterdayConsumedNutrientsDialogFragment
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
        consumedNutrientsViewModel.getYesterdayConsumedNutrients()

        consumedNutrientsViewModel.yesterdayConsumedNutrientsSingleLiveEvent.observe(viewLifecycleOwner) {
            val dialogFragment = YesterdayConsumedNutrientsDialogFragment(it)
            dialogFragment.show(childFragmentManager, "yesterday_consumed_nutrients_dialog_fragment")
        }

        binding.waterCard.setOnClickListener {
            val dialogFragment = NutrientDialogFragment(
                requireContext().getString(R.string.water).lowercase()
            )
            dialogFragment.show(childFragmentManager, "water_dialog_fragment")
        }

        binding.proteinsCard.setOnClickListener {
            val dialogFragment = NutrientDialogFragment(
                requireContext().getString(R.string.proteins).lowercase()
            )
            dialogFragment.show(childFragmentManager, "protein_dialog_fragment")
        }

        binding.carbsCard.setOnClickListener {
            val dialogFragment = NutrientDialogFragment(
                requireContext().getString(R.string.carbs).lowercase()
            )
            dialogFragment.show(childFragmentManager, "carb_dialog_fragment")
        }

        binding.fatsCard.setOnClickListener {
            val dialogFragment = NutrientDialogFragment(
                requireContext().getString(R.string.fats).lowercase()
            )
            dialogFragment.show(childFragmentManager, "fat_dialog_fragment")
        }

        binding.fiberCard.setOnClickListener {
            val dialogFragment = NutrientDialogFragment(
                requireContext().getString(R.string.fiber).lowercase()
            )
            dialogFragment.show(childFragmentManager, "fiber_dialog_fragment")
        }

        binding.sugarCard.setOnClickListener {
            val dialogFragment = NutrientDialogFragment(
                requireContext().getString(R.string.sugar).lowercase()
            )
            dialogFragment.show(childFragmentManager, "sugar_dialog_fragment")
        }

        binding.saltCard.setOnClickListener {
            val dialogFragment = NutrientDialogFragment(
                requireContext().getString(R.string.salt).lowercase()
            )
            dialogFragment.show(childFragmentManager, "salt_dialog_fragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}