package kyrylost.apps.eatwise.room.favoritefood

import kyrylost.apps.eatwise.model.FavoriteFood
import javax.inject.Inject

class FavoriteFoodRepository @Inject constructor(
    private val favoriteFoodDao: FavoriteFoodDao
) {
    fun insertFavoriteFood(favoriteFood: FavoriteFood) =
        favoriteFoodDao.insertFavoriteFood(favoriteFood)

    fun getFavoriteFoodList(): List<FavoriteFood> =
        favoriteFoodDao.getFavoriteFoodList()

    fun deleteFavoriteFood(favoriteFood: FavoriteFood) =
        favoriteFoodDao.deleteFavoriteFood(favoriteFood)
}