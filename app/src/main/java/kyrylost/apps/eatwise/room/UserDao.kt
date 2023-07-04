package kyrylost.apps.eatwise.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kyrylost.apps.eatwise.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: User) //birthDate: Date, weight: Double, sex: Int, work: Int, trainings: Int, diet: Int

    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): User?

}