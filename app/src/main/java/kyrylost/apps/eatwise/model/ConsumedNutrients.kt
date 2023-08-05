package kyrylost.apps.eatwise.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumed_nutrients")
data class ConsumedNutrients(
    @PrimaryKey var id : Int = 1,
    @ColumnInfo(name = "calories") val calories: Double = 0.0,
    @ColumnInfo(name = "water") val water: Double = 0.0,
    @ColumnInfo(name = "proteins") val proteins: Double = 0.0,
    @ColumnInfo(name = "carbs") val carbs: Double = 0.0,
    @ColumnInfo(name = "fats") val fats: Double = 0.0,
    @ColumnInfo(name = "fiber") val fiber: Double = 0.0,
    @ColumnInfo(name = "sugar") val sugar: Double = 0.0,
    @ColumnInfo(name = "salt") val salt: Double = 0.0
)
