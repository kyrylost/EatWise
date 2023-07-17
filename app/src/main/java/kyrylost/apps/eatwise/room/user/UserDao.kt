package kyrylost.apps.eatwise.room.user

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

    @Query("UPDATE user SET weight = :newWeight WHERE id = 1")
    fun updateWeight(newWeight: Double)

    @Query("UPDATE user SET sex = :newSex WHERE id = 1")
    fun updateSex(newSex: Int)

    @Query("UPDATE user SET work = :newWork WHERE id = 1")
    fun updateWork(newWork: Int)

    @Query("UPDATE user SET trainings = :newTrainings WHERE id = 1")
    fun updateTrainings(newTrainings: Int)

    @Query("UPDATE user SET diet = :newDiet WHERE id = 1")
    fun updateDiet(newDiet: Int)
}