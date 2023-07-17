package kyrylost.apps.eatwise.room.favoritefood

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kyrylost.apps.eatwise.model.FavoriteFood

@Dao
interface FavoriteFoodDao {
    @Insert
    fun insertFavoriteFood(favoriteFood: FavoriteFood)

    @Query("SELECT * FROM favorite_food")
    fun getFavoriteFoodList(): List<FavoriteFood>

    @Delete
    fun deleteFavoriteFood(favoriteFood: FavoriteFood)
}