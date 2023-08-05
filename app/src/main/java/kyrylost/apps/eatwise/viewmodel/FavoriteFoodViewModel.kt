package kyrylost.apps.eatwise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
        CoroutineScope(Dispatchers.IO).launch {
            favoriteFoodRepository.insertFavoriteFood(favoriteFood)
        }
    }

    fun getFavoriteFoodList() {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteFoodLiveData.postValue(LinkedList(favoriteFoodRepository.getFavoriteFoodList()))
        }
    }

    fun deleteFavoriteFood(favoriteFood: FavoriteFood) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteFoodRepository.deleteFavoriteFood(favoriteFood)
        }
    }

}