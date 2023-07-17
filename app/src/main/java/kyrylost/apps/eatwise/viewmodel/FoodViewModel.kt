package kyrylost.apps.eatwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kyrylost.apps.eatwise.foodapi.FoodApiRepository
import kyrylost.apps.eatwise.model.FoodSearchResponse
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodApiRepository: FoodApiRepository
): ViewModel() {

    val apiKey =  "fdRZzjj40XfaiBJhfE5y64iehfuJj6vh6A2eZYFI"

    fun searchFood(query: String): Flow<PagingData<FoodSearchResponse.Food>> =
        foodApiRepository.searchFoods(query, apiKey).cachedIn(viewModelScope)

}