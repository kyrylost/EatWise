package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.adapters.OwnFoodAdapter
import kyrylost.apps.eatwise.databinding.OwnFoodFragmentBinding
import kyrylost.apps.eatwise.fragments.dialogs.AddFoodDialogFragment
import kyrylost.apps.eatwise.fragments.dialogs.FoodDialogFragment
import kyrylost.apps.eatwise.viewmodel.OwnFoodViewModel

class OwnFoodFragment: Fragment() {
    private var _binding: OwnFoodFragmentBinding? = null
    private val binding get() = _binding!!
    private val ownFoodViewModel: OwnFoodViewModel by activityViewModels()
    private lateinit var adapter: OwnFoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OwnFoodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ownFoodViewModel.getOwnFoodList()

        ownFoodViewModel.ownFoodLiveData.observe(viewLifecycleOwner) {
            adapter = OwnFoodAdapter(it, requireContext())
            binding.ownFoodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.ownFoodRecyclerView.adapter = adapter
            adapter.onItemClick = { foodData ->
                val dialogFragment = FoodDialogFragment(foodData)
                dialogFragment.show(childFragmentManager, "selected_food_dialog_fragment")
            }
            adapter.onDeleteClick = { ownFood ->
                ownFoodViewModel.deleteOwnFood(ownFood)
            }
        }

        binding.searchTb.setOnClickListener {
            val navController = OwnFoodFragmentDirections.actionOwnFoodFragmentToFoodListFragment()
            findNavController().navigate(navController)
        }

        binding.favoriteTb.setOnClickListener {
            val navController = OwnFoodFragmentDirections.actionOwnFoodFragmentToFavoriteFoodFragment()
            findNavController().navigate(navController)
        }

        binding.addOwnFoodBtn.setOnClickListener {
            val dialogFragment = AddFoodDialogFragment()
            dialogFragment.show(childFragmentManager, "add_food_dialog_fragment")
        }

        subscribeToObservables()

    }

    private fun subscribeToObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ownFoodViewModel.ownFoodInserted.collectLatest {
                    Toast.makeText(
                        context,
                        "Own  food was successfully added!",
                        Toast.LENGTH_LONG
                    ).show()

                    (childFragmentManager.findFragmentByTag(
                        "add_food_dialog_fragment"
                    ) as DialogFragment).dismiss()

                    adapter.addNewElement(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}