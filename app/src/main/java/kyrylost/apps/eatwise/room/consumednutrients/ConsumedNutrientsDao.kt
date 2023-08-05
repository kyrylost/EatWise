package kyrylost.apps.eatwise.room.consumednutrients

import androidx.room.*
import kyrylost.apps.eatwise.model.ConsumedNutrients

@Dao
interface ConsumedNutrientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConsumedNutrients(consumedNutrients: ConsumedNutrients)

    @Query("SELECT * FROM consumed_nutrients WHERE id = :id")
    fun getConsumedNutrients(id: Int): ConsumedNutrients?

    @Delete
    fun deleteConsumedNutrients(consumedNutrients: ConsumedNutrients)

    @Query("UPDATE consumed_nutrients SET calories = :updatedValue WHERE id = 1")
    fun updateConsumedCalories(updatedValue: Double)

    @Query("UPDATE consumed_nutrients SET water = :updatedValue WHERE id = 1")
    fun updateConsumedWater(updatedValue: Double)

    @Query("UPDATE consumed_nutrients SET proteins = :updatedValue WHERE id = 1")
    fun updateConsumedProteins(updatedValue: Double)

    @Query("UPDATE consumed_nutrients SET carbs = :updatedValue WHERE id = 1")
    fun updateConsumedCarbs(updatedValue: Double)

    @Query("UPDATE consumed_nutrients SET fats = :updatedValue WHERE id = 1")
    fun updateConsumedFats(updatedValue: Double)

    @Query("UPDATE consumed_nutrients SET fiber = :updatedValue WHERE id = 1")
    fun updateConsumedFiber(updatedValue: Double)

    @Query("UPDATE consumed_nutrients SET sugar = :updatedValue WHERE id = 1")
    fun updateConsumedSugar(updatedValue: Double)

    @Query("UPDATE consumed_nutrients SET salt = :updatedValue WHERE id = 1")
    fun updateConsumedSalt(updatedValue: Double)

    @Update
    fun resetConsumedNutrients(row: ConsumedNutrients)

}