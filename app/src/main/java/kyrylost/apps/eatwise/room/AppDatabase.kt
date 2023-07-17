package kyrylost.apps.eatwise.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kyrylost.apps.eatwise.model.*
import kyrylost.apps.eatwise.room.consumednutrients.ConsumedNutrientsDao
import kyrylost.apps.eatwise.room.favoritefood.FavoriteFoodDao
import kyrylost.apps.eatwise.room.ownrecipes.OwnFoodDao
import kyrylost.apps.eatwise.room.user.UserDao

@Database(entities = [
    User::class,
    ConsumedNutrients::class,
    FavoriteFood::class,
    OwnFood::class],
    version = 1)
@TypeConverters(FoodDataTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun consumedNutrientsDao(): ConsumedNutrientsDao
    abstract fun favoriteFoodDao(): FavoriteFoodDao
    abstract fun ownFoodDao(): OwnFoodDao

}
