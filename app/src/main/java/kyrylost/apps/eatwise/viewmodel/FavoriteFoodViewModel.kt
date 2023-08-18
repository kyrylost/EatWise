package kyrylost.apps.eatwise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.model.FavoriteFood
import kyrylost.apps.eatwise.room.favoritefood.FavoriteFoodRepository
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class FavoriteFoodViewModel @Inject constructor(
    private val favoriteFoodRepository: FavoriteFoodRepository
): ViewModel() {

    val favoriteFoodLiveData = MutableLiveData<LinkedList<FavoriteFood>>()

    fun insertFavoriteFood(favoriteFood: FavoriteFood) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteFoodRepository.insertFavoriteFood(favoriteFood)
        }
    }

    fun getFavoriteFoodList() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteFoodLiveData.postValue(LinkedList(favoriteFoodRepository.getFavoriteFoodList()))
        }
    }

    fun deleteFavoriteFood(favoriteFood: FavoriteFood) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteFoodRepository.deleteFavoriteFood(favoriteFood)
        }
    }

}