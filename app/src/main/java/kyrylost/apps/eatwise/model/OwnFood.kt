package kyrylost.apps.eatwise.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "own_food")
data class OwnFood (
    @PrimaryKey(autoGenerate = true) val id : Int? = null,
    val foodData: FoodData
)