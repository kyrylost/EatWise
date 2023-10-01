package kyrylost.apps.eatwise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.model.FoodData
import kyrylost.apps.eatwise.model.OwnFood
import kyrylost.apps.eatwise.room.ownrecipes.OwnFoodRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OwnFoodViewModel @Inject constructor(
    private val ownFoodRepository: OwnFoodRepository
): ViewModel() {

    private val _ownFoodLiveData = MutableLiveData<LinkedList<OwnFood>>()
    val ownFoodLiveData : LiveData<LinkedList<OwnFood>> = _ownFoodLiveData

    private val _emptyFieldError = MutableSharedFlow<String>()
    val emptyFieldError = _emptyFieldError.asSharedFlow()

    private val _ownFoodInserted = MutableSharedFlow<OwnFood>()
    val ownFoodInserted = _ownFoodInserted.asSharedFlow()

    fun insertOwnFood(
        foodName: String?, foodCalories: String?,
        foodWater: String?, foodProtein: String?,
        foodCarbs: String?, foodFats: String?,
        foodFiber: String?, foodSugar: String?,
        foodSalt: String?
    ) = viewModelScope.launch {

        if (foodName.isNullOrEmpty()) {
            _emptyFieldError.emit("Name field have to be filled!")
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val foodData = FoodData(
                    foodName,
                    foodCalories?.toDoubleOrNull() ?: 0.0,
                    foodWater?.toDoubleOrNull() ?: 0.0,
                    foodProtein?.toDoubleOrNull()  ?: 0.0,
                    foodCarbs?.toDoubleOrNull()  ?: 0.0,
                    foodFats?.toDoubleOrNull()  ?: 0.0,
                    foodFiber?.toDoubleOrNull() ?: 0.0,
                    foodSugar?.toDoubleOrNull() ?: 0.0,
                    foodSalt?.toDoubleOrNull() ?: 0.0
                )
                val ownFood = OwnFood(foodData = foodData)
                ownFoodRepository.insertOwnFood(ownFood).also {
                    _ownFoodInserted.emit(ownFoodRepository.getLast())
                }
            }
        }
    }

    fun getOwnFoodList() {
        viewModelScope.launch(Dispatchers.IO) {
            _ownFoodLiveData.postValue(LinkedList(ownFoodRepository.getOwnFoodList()))
        }
    }

    fun deleteOwnFood(ownFood: OwnFood) {
        viewModelScope.launch(Dispatchers.IO) {
            ownFoodRepository.deleteOwnFood(ownFood)
        }
    }

}