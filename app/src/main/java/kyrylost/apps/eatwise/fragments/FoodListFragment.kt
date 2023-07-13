package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.adapters.FoodAdapter
import kyrylost.apps.eatwise.databinding.FoodListFragmentBinding
import kyrylost.apps.eatwise.fragments.dialogs.FoodDialogFragment
import kyrylost.apps.eatwise.viewmodel.FoodViewModel

class FoodListFragment : Fragment() {
    private var _binding: FoodListFragmentBinding? = null
    private val binding get() = _binding!!
    private val foodViewModel: FoodViewModel by activityViewModels()
    private var adapter: FoodAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FoodListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        binding.searchBtn.setOnClickListener {
            val searchedFood = binding.searchET.text.toString()
            collectUiState(searchedFood)
        }
    }

    private fun initView() {
        adapter = FoodAdapter(requireContext())
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.foodRecyclerView.adapter = adapter
        adapter?.onItemClick = {
            val dialogFragment = FoodDialogFragment(it)
            dialogFragment.show(childFragmentManager, "selected_food_dialog_fragment")
        }
    }

    private fun collectUiState(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            foodViewModel.searchFood(query).collectLatest { food ->
                launch(Dispatchers.Main) {
                    adapter?.loadStateFlow?.collectLatest { loadStates ->

                        val stateRefresh = loadStates.refresh
                        val stateAppend = loadStates.append

                        binding.progressBar.isVisible = stateRefresh is LoadState.Loading ||
                                stateAppend is LoadState.Loading

                        binding.errorLayout.isVisible = stateRefresh is LoadState.Error ||
                                stateAppend is LoadState.Error

                        binding.refreshButton.setOnClickListener {
                            binding.errorLayout.isVisible = false
                            collectUiState(query)
                        }

                    }
                }

                adapter?.submitData(food)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}