package kyrylost.apps.eatwise.foodapi

import kyrylost.apps.eatwise.model.FoodSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {
    @GET("foods/search")
    suspend fun searchFoods(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<FoodSearchResponse>
}