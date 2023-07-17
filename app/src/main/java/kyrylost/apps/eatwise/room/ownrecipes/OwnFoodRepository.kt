package kyrylost.apps.eatwise.room.ownrecipes

import kyrylost.apps.eatwise.model.OwnFood
import javax.inject.Inject

class OwnFoodRepository @Inject constructor(
    private val ownFoodDao: OwnFoodDao
) {
    fun insertOwnFood(ownFood: OwnFood) =
        ownFoodDao.insertOwnFood(ownFood)

    fun getOwnFoodList(): List<OwnFood> =
        ownFoodDao.getOwnFoodList()

    fun deleteOwnFood(ownFood: OwnFood) =
        ownFoodDao.deleteOwnFood(ownFood)

    fun getLast(): OwnFood =
        ownFoodDao.getLast()
}