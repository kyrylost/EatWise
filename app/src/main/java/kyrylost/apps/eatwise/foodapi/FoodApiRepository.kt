package kyrylost.apps.eatwise.foodapi

import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class FoodApiRepository @Inject constructor(private val foodApiService: FoodApiService) {

    fun searchFoods(query: String, apiKey: String) = Pager(
        config = PagingConfig(pageSize = 8),
        pagingSourceFactory = {
            FoodPagingSource(foodApiService, apiKey, query)
        }
    ).flow

}