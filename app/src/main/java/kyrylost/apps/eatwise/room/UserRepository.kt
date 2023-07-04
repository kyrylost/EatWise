package kyrylost.apps.eatwise.room

import kyrylost.apps.eatwise.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun createUser(user: User) = userDao.createUser(user)
    fun getUser() : User? = userDao.getUser()

}