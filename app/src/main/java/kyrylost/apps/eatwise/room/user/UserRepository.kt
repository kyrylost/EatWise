package kyrylost.apps.eatwise.room.user

import kyrylost.apps.eatwise.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun createUser(user: User) = userDao.createUser(user)
    fun getUser() : User? = userDao.getUser()
    fun updateWeight(weight: Double) = userDao.updateWeight(weight)
    fun updateSex(sex: Int) = userDao.updateSex(sex)
    fun updateWork(work: Int) = userDao.updateWork(work)
    fun updateTrainings(Trainings: Int) = userDao.updateTrainings(Trainings)
    fun updateDiet(diet: Int) = userDao.updateDiet(diet)

}