package kyrylost.apps.eatwise.foodapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FoodApiInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nal.usda.gov/fdc/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: FoodApiService by lazy {
        retrofit.create(FoodApiService::class.java)
    }
}