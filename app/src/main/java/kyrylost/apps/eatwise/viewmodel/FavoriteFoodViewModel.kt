package kyrylost.apps.eatwise.viewmodel

import androidx.lifecycle.LiveData
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

    private val _favoriteFoodLiveData = MutableLiveData<LinkedList<FavoriteFood>>()
    val favoriteFoodLiveData: LiveData<LinkedList<FavoriteFood>> = _favoriteFoodLiveData

    fun insertFavoriteFood(favoriteFood: FavoriteFood) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteFoodRepository.insertFavoriteFood(favoriteFood)
        }
    }

    fun getFavoriteFoodList() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteFoodLiveData.postValue(LinkedList(favoriteFoodRepository.getFavoriteFoodList()))
        }
    }

    fun deleteFavoriteFood(favoriteFood: FavoriteFood) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteFoodRepository.deleteFavoriteFood(favoriteFood)
        }
    }

}