package kyrylost.apps.eatwise

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kyrylost.apps.eatwise.foodapi.FoodApiInstance
import kyrylost.apps.eatwise.foodapi.FoodApiRepository
import kyrylost.apps.eatwise.room.*
import kyrylost.apps.eatwise.room.consumednutrients.ConsumedNutrientsDao
import kyrylost.apps.eatwise.room.consumednutrients.ConsumedNutrientsRepository
import kyrylost.apps.eatwise.room.favoritefood.FavoriteFoodDao
import kyrylost.apps.eatwise.room.favoritefood.FavoriteFoodRepository
import kyrylost.apps.eatwise.room.ownrecipes.OwnFoodDao
import kyrylost.apps.eatwise.room.ownrecipes.OwnFoodRepository
import kyrylost.apps.eatwise.room.user.UserDao
import kyrylost.apps.eatwise.room.user.UserRepository

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideAppDatabase(application: Application): AppDatabase{
        return Room.databaseBuilder(
            application, AppDatabase::class.java, "User and Consumed Nutrients"
        ).build()
    }

    // Providing dependencies for consumed nutrients
    @Provides
    fun provideConsumedNutrientsDao(appDatabase: AppDatabase): ConsumedNutrientsDao {
        return appDatabase.consumedNutrientsDao()
    }

    @Provides
    fun provideConsumedNutrientsRepository(
        consumedNutrientsDao: ConsumedNutrientsDao
    ) : ConsumedNutrientsRepository {
        return ConsumedNutrientsRepository(consumedNutrientsDao)
    }


    // Providing dependencies for user
    @Provides
    fun provideUserDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideUserRepository(userDao: UserDao) : UserRepository {
        return UserRepository(userDao)
    }


    // Providing dependencies for favorite food
    @Provides
    fun provideFavoriteFoodDao(appDatabase: AppDatabase) : FavoriteFoodDao {
        return appDatabase.favoriteFoodDao()
    }

    @Provides
    fun provideFavoriteFoodRepository(favoriteFoodDao: FavoriteFoodDao) : FavoriteFoodRepository {
        return FavoriteFoodRepository(favoriteFoodDao)
    }


    // Providing dependencies for own recipes
    @Provides
    fun provideOwnFoodDao(appDatabase: AppDatabase) : OwnFoodDao {
        return appDatabase.ownFoodDao()
    }

    @Provides
    fun provideOwnFoodRepository(ownFoodDao: OwnFoodDao) : OwnFoodRepository {
        return OwnFoodRepository(ownFoodDao)
    }


    // Providing dependencies for food API
    @Provides
    fun provideFoodApiRepository() : FoodApiRepository {
        return FoodApiRepository(FoodApiInstance.apiService)
    }
}