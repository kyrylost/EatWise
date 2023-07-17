package kyrylost.apps.eatwise.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import kyrylost.apps.eatwise.model.FoodData

class FoodDataTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromFoodData(foodData: FoodData): String {
        return gson.toJson(foodData)
    }

    @TypeConverter
    fun toFoodData(value: String): FoodData {
        return gson.fromJson(value, FoodData::class.java)
    }
}