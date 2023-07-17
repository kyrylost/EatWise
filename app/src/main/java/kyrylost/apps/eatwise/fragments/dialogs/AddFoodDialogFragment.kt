package kyrylost.apps.eatwise.fragments.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import kyrylost.apps.eatwise.databinding.AddFoodDialogFragmentBinding
import kyrylost.apps.eatwise.viewmodel.OwnFoodViewModel

class AddFoodDialogFragment : DialogFragment() {

    private var _binding: AddFoodDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val ownFoodViewModel: OwnFoodViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = AddFoodDialogFragmentBinding.inflate(layoutInflater)

        binding.addBtn.setOnClickListener {
            val foodName = binding.foodNameEt.text?.toString()
            val foodCalories = binding.caloriesEt.text?.toString()
            val foodWater = binding.waterEt.text?.toString()
            val foodProtein = binding.proteinEt.text?.toString()
            val foodCarbs = binding.carbsEt.text?.toString()
            val foodFats = binding.fatsEt.text?.toString()
            val foodFiber = binding.fiberEt.text?.toString()
            val foodSugar = binding.sugarEt.text?.toString()
            val foodSalt = binding.saltEt.text?.toString()

            ownFoodViewModel.insertOwnFood(
                foodName, foodCalories,
                foodWater, foodProtein,
                foodCarbs, foodFats,
                foodFiber, foodSugar,
                foodSalt
            )
        }

        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ownFoodViewModel.emptyFieldErrorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}