package kyrylost.apps.eatwise.foodapi

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kyrylost.apps.eatwise.model.FoodSearchResponse

class FoodPagingSource(
    private val foodApiService: FoodApiService,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, FoodSearchResponse.Food>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FoodSearchResponse.Food> {
        val pageNumber = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val response = foodApiService.searchFoods(query, apiKey, pageNumber, pageSize)
            val foodList = response.body()?.foods ?: emptyList()

            LoadResult.Page(
                data = foodList,
                prevKey = if (pageNumber > 1) pageNumber - 1 else null,
                nextKey = if (pageNumber < (response.body()?.totalPages ?: 1)) pageNumber + 1 else null
            )
        } catch (e: Exception) {
            Log.e("FoodPagingSource Load Error", e.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FoodSearchResponse.Food>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}