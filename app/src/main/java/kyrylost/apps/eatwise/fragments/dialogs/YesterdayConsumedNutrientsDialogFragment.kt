package kyrylost.apps.eatwise.fragments.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kyrylost.apps.eatwise.databinding.YeasterdayConsumedNutrientsBinding
import kyrylost.apps.eatwise.model.ConsumedNutrients

class YesterdayConsumedNutrientsDialogFragment(
    private val consumedNutrients: ConsumedNutrients): DialogFragment() {

    private var _binding: YeasterdayConsumedNutrientsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = YeasterdayConsumedNutrientsBinding.inflate(layoutInflater)

        binding.calories.text = consumedNutrients.calories.toString()
        binding.water.text = consumedNutrients.water.toString()
        binding.proteins.text = consumedNutrients.proteins.toString()
        binding.carbs.text = consumedNutrients.carbs.toString()
        binding.fats.text = consumedNutrients.fats.toString()
        binding.fiber.text = consumedNutrients.fiber.toString()
        binding.sugar.text= consumedNutrients.sugar.toString()
        binding.salt.text = consumedNutrients.salt.toString()

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