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
import kyrylost.apps.eatwise.databinding.NutrientDialogFragmentBinding
import kyrylost.apps.eatwise.viewmodel.ConsumedNutrientsViewModel

class NutrientDialogFragment(private val nutrient: String): DialogFragment() {

    private var _binding: NutrientDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val consumedNutrientsViewModel: ConsumedNutrientsViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = NutrientDialogFragmentBinding.inflate(layoutInflater)

        if (nutrient == requireContext().getString(R.string.water).lowercase()) {

            val consumedWater = consumedNutrientsViewModel.consumedWater.get()
            val consumedWaterPercentage = (consumedNutrientsViewModel.consumedWater.get() /
                    consumedNutrientsViewModel.recommendedWater.get()) * 100

            binding.title.text = String.format(
                requireContext().getString(R.string.dialog_water_title_template),
                consumedWater, nutrient, consumedWaterPercentage)

            binding.subtitle.text = String.format(requireContext().getString(R.string.dialog_subtitle_template),
                nutrient)

            binding.note.text = requireContext().getString(R.string.dialog_water_note)

            binding.nutrientAndUnit.text = nutrient.replaceFirstChar(Char::uppercase) +
                    requireContext().getString(R.string.ml)

            binding.addBtn.setOnClickListener {
                val consumedNutrient = binding.addET.text?.toString()?.toDoubleOrNull()?.div(1000)
                consumedNutrientsViewModel.updateConsumedWater(consumedNutrient)
                dismiss()
            }

        }

        if (nutrient == requireContext().getString(R.string.proteins).lowercase()) {

            val consumedProteins = consumedNutrientsViewModel.consumedProteins.get()
            val consumedProteinsPercentage = (consumedNutrientsViewModel.consumedProteins.get() /
                    consumedNutrientsViewModel.recommendedProteins.get()) * 100

            binding.title.text = String.format(
                requireContext().getString(R.string.dialog_title_template),
                consumedProteins, nutrient, consumedProteinsPercentage)

            binding.subtitle.text = String.format(requireContext().getString(R.string.dialog_subtitle_template),
                nutrient)

            binding.note.text = requireContext().getString(R.string.dialog_protein_note)

            binding.nutrientAndUnit.text = nutrient.replaceFirstChar(Char::uppercase) +
                    requireContext().getString(R.string.g)

            binding.addBtn.setOnClickListener {
                val consumedNutrient = binding.addET.text?.toString()?.toDoubleOrNull()
                consumedNutrientsViewModel.updateConsumedProteins(consumedNutrient)
                dismiss()
            }

        }

        if (nutrient == requireContext().getString(R.string.carbs).lowercase()) {

            val consumedCarbs = consumedNutrientsViewModel.consumedCarbs.get()
            val consumedCarbsPercentage = (consumedNutrientsViewModel.consumedCarbs.get() /
                    consumedNutrientsViewModel.recommendedCarbs.get()) * 100

            binding.title.text = String.format(
                requireContext().getString(R.string.dialog_title_template),
                consumedCarbs, nutrient, consumedCarbsPercentage)

            binding.subtitle.text = String.format(requireContext().getString(R.string.dialog_subtitle_template),
                nutrient)

            binding.note.text = requireContext().getString(R.string.dialog_carb_note)

            binding.nutrientAndUnit.text = nutrient.replaceFirstChar(Char::uppercase) +
                    requireContext().getString(R.string.g)

            binding.addBtn.setOnClickListener {
                val consumedNutrient = binding.addET.text?.toString()?.toDoubleOrNull()
                consumedNutrientsViewModel.updateConsumedCarbs(consumedNutrient)
                dismiss()
            }

        }

        if (nutrient == requireContext().getString(R.string.fats).lowercase()) {

            val consumedFats = consumedNutrientsViewModel.consumedFats.get()
            val consumedFatsPercentage = (consumedNutrientsViewModel.consumedFats.get() /
                    consumedNutrientsViewModel.recommendedFats.get()) * 100

            binding.title.text = String.format(
                requireContext().getString(R.string.dialog_title_template),
                consumedFats, nutrient, consumedFatsPercentage)

            binding.subtitle.text = String.format(requireContext().getString(R.string.dialog_subtitle_template),
                nutrient)

            binding.note.text = requireContext().getString(R.string.dialog_fat_note)

            binding.nutrientAndUnit.text = nutrient.replaceFirstChar(Char::uppercase) +
                    requireContext().getString(R.string.g)

            binding.addBtn.setOnClickListener {
                val consumedNutrient = binding.addET.text?.toString()?.toDoubleOrNull()
                consumedNutrientsViewModel.updateConsumedFats(consumedNutrient)
                dismiss()
            }

        }

        if (nutrient == requireContext().getString(R.string.fiber).lowercase()) {

            val consumedFiber = consumedNutrientsViewModel.consumedFiber.get()
            val consumedFiberPercentage = (consumedFiber /
                    consumedNutrientsViewModel.recommendedFiber) * 100

            binding.title.text = String.format(
                requireContext().getString(R.string.dialog_title_template),
                consumedFiber, nutrient, consumedFiberPercentage)

            binding.subtitle.text = String.format(requireContext().getString(R.string.dialog_subtitle_template),
                nutrient)

            binding.note.text = requireContext().getString(R.string.dialog_fiber_note)

            binding.nutrientAndUnit.text = nutrient.replaceFirstChar(Char::uppercase) +
                    requireContext().getString(R.string.g)

            binding.addBtn.setOnClickListener {
                val consumedNutrient = binding.addET.text?.toString()?.toDoubleOrNull()
                consumedNutrientsViewModel.updateConsumedFiber(consumedNutrient)
                dismiss()
            }

        }

        if (nutrient == requireContext().getString(R.string.sugar).lowercase()) {

            val consumedSugar = consumedNutrientsViewModel.consumedSugar.get()
            val consumedSugarPercentage = (consumedSugar /
                    consumedNutrientsViewModel.recommendedSugar.get()) * 100


            binding.title.text = String.format(
                requireContext().getString(R.string.dialog_title_template),
                consumedSugar, nutrient, consumedSugarPercentage)

            binding.subtitle.text = String.format(requireContext().getString(R.string.dialog_subtitle_template),
                nutrient)

            binding.note.text = requireContext().getString(R.string.dialog_sugar_note)

            binding.nutrientAndUnit.text = nutrient.replaceFirstChar(Char::uppercase) +
                    requireContext().getString(R.string.g)

            binding.addBtn.setOnClickListener {
                val consumedNutrient = binding.addET.text?.toString()?.toDoubleOrNull()
                consumedNutrientsViewModel.updateConsumedSugar(consumedNutrient)
                dismiss()
            }

        }

        if (nutrient == requireContext().getString(R.string.salt).lowercase()) {

            val consumedSalt = consumedNutrientsViewModel.consumedSalt.get()
            val consumedSaltPercentage = (consumedSalt /
                    consumedNutrientsViewModel.recommendedSalt) * 100

            binding.title.text = String.format(
                requireContext().getString(R.string.dialog_title_template),
                consumedSalt, nutrient, consumedSaltPercentage)

            binding.subtitle.text = String.format(requireContext().getString(R.string.dialog_subtitle_template),
                nutrient)

            binding.note.text = requireContext().getString(R.string.dialog_salt_note)

            binding.nutrientAndUnit.text = nutrient.replaceFirstChar(Char::uppercase) +
                    requireContext().getString(R.string.g)

            binding.addBtn.setOnClickListener {
                val consumedNutrient = binding.addET.text?.toString()?.toDoubleOrNull()
                consumedNutrientsViewModel.updateConsumedSalt(consumedNutrient)
                dismiss()
            }

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