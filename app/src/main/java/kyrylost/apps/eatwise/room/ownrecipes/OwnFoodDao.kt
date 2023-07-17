package kyrylost.apps.eatwise.room.ownrecipes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kyrylost.apps.eatwise.model.OwnFood

@Dao
interface OwnFoodDao {
    @Insert
    fun insertOwnFood(ownFood: OwnFood)

    @Query("SELECT * FROM own_food")
    fun getOwnFoodList(): List<OwnFood>

    @Delete
    fun deleteOwnFood(ownFood: OwnFood)

    @Query("SELECT * FROM own_food ORDER BY ID DESC LIMIT 1")
    fun getLast(): OwnFood
}