package kyrylost.apps.eatwise.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kyrylost.apps.eatwise.model.ConsumedNutrients

@Dao
interface ConsumedNutrientsDao {

    @Insert
    fun insertConsumedNutrients(consumedNutrients: ConsumedNutrients)

    @Query("UPDATE consumed_nutrients SET calories = :updatedValue WHERE id = 1")
    fun updateConsumedCalories(updatedValue: Double)

    @Query("SELECT * FROM consumed_nutrients WHERE id = 1")
    fun getConsumedNutrients(): ConsumedNutrients?

    @Update
    fun resetConsumedNutrients(row: ConsumedNutrients)

}