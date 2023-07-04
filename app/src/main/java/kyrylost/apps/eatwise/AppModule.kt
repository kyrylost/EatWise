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


    // Providing dependencies for food API
    @Provides
    fun provideFoodApiRepository() : FoodApiRepository {
        return FoodApiRepository(FoodApiInstance.apiService)
    }
}