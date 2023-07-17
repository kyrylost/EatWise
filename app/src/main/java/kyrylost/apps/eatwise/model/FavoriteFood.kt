package kyrylost.apps.eatwise.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_food")
data class FavoriteFood(
    @PrimaryKey(autoGenerate = true) val id : Int? = null,
    val foodData: FoodData
)
