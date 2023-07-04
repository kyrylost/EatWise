package kyrylost.apps.eatwise.room

import kyrylost.apps.eatwise.model.ConsumedNutrients
import javax.inject.Inject

class ConsumedNutrientsRepository @Inject constructor(
    private val consumedNutrientsDao: ConsumedNutrientsDao
) {

    fun insertConsumedNutrients(consumedNutrients: ConsumedNutrients) =
        consumedNutrientsDao.insertConsumedNutrients(consumedNutrients)

    fun updateConsumedCalories(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedCalories(updatedValue)

    fun getConsumedNutrients() =
        consumedNutrientsDao.getConsumedNutrients()

}