package kyrylost.apps.eatwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kyrylost.apps.eatwise.adapters.FavoriteFoodAdapter
import kyrylost.apps.eatwise.databinding.FavoriteFoodFragmentBinding
import kyrylost.apps.eatwise.fragments.dialogs.FoodDialogFragment
import kyrylost.apps.eatwise.viewmodel.FavoriteFoodViewModel

class FavoriteFoodFragment : Fragment() {
    private var _binding: FavoriteFoodFragmentBinding? = null
    private val binding get() = _binding!!
    private val favoriteFoodViewModel: FavoriteFoodViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFoodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteFoodViewModel.getFavoriteFoodList()

        binding.searchTb.setOnClickListener {
            val navController = FavoriteFoodFragmentDirections.actionFavoriteFoodFragmentToFoodListFragment()
            findNavController().navigate(navController)
        }

        binding.ownTb.setOnClickListener {
            val navController = FavoriteFoodFragmentDirections.actionFavoriteFoodFragmentToOwnFoodFragment()
            findNavController().navigate(navController)
        }

        favoriteFoodViewModel.favoriteFoodLiveData.observe(viewLifecycleOwner) {
            val adapter = FavoriteFoodAdapter(it, requireContext())
            binding.favoriteFoodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.favoriteFoodRecyclerView.adapter = adapter
            adapter.onItemClick = { foodData ->
                val dialogFragment = FoodDialogFragment(foodData)
                dialogFragment.show(childFragmentManager, "selected_food_dialog_fragment")
            }
            adapter.onAddToFavoriteClick = { favoriteFood ->
                favoriteFoodViewModel.deleteFavoriteFood(favoriteFood)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}