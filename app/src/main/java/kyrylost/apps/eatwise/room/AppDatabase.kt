package kyrylost.apps.eatwise.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kyrylost.apps.eatwise.model.ConsumedNutrients
import kyrylost.apps.eatwise.model.User

@Database(entities = [User::class, ConsumedNutrients::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun consumedNutrientsDao(): ConsumedNutrientsDao
}
