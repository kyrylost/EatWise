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

    fun updateConsumedWater(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedWater(updatedValue)

    fun updateConsumedProteins(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedProteins(updatedValue)

    fun updateConsumedCarbs(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedCarbs(updatedValue)

    fun updateConsumedFats(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedFats(updatedValue)

    fun updateConsumedFiber(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedFiber(updatedValue)

    fun updateConsumedSugar(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedSugar(updatedValue)

    fun updateConsumedSalt(updatedValue: Double) =
        consumedNutrientsDao.updateConsumedSalt(updatedValue)

    fun getConsumedNutrients() =
        consumedNutrientsDao.getConsumedNutrients()

}