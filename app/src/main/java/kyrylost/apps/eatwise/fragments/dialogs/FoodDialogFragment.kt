package kyrylost.apps.eatwise.fragments.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import kyrylost.apps.eatwise.R
import kyrylost.apps.eatwise.databinding.FoodDialogFragmentBinding
import kyrylost.apps.eatwise.model.FoodData
import kyrylost.apps.eatwise.viewmodel.ConsumedNutrientsViewModel

class FoodDialogFragment(private val foodData: FoodData) : DialogFragment() {

    private var _binding: FoodDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val consumedNutrientsViewModel: ConsumedNutrientsViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FoodDialogFragmentBinding.inflate(layoutInflater)

        binding.title.text = foodData.description
        binding.foodAndUnit.text = foodData.description +
                requireContext().getString(R.string.g)

        binding.addBtn.setOnClickListener {
            val amount = binding.addET.text?.toString()?.toDoubleOrNull()
            consumedNutrientsViewModel.updateNutrientsByFoodAmountAndData(amount, foodData)
            dismiss()
        }

        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}