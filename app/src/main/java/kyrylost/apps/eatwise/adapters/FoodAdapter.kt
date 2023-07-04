package kyrylost.apps.eatwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kyrylost.apps.eatwise.databinding.FoodItemBinding
import kyrylost.apps.eatwise.model.FoodSearchResponse.Food
import kyrylost.apps.eatwise.viewholders.FoodViewHolder

class FoodAdapter(private val context: Context) : PagingDataAdapter<Food, FoodViewHolder>(FoodDiffCallBack()) {

    class FoodDiffCallBack : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.fdcId == newItem.fdcId
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            FoodItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            val calories = item.foodNutrients.find {
                it.nutrientId == 1008
            }?.value ?: 0.0

            val proteins = item.foodNutrients.find {
                it.nutrientId == 1003
            }?.value ?: 0.0

            val carbs = item.foodNutrients.find {
                it.nutrientId == 1005
            }?.value ?: 0.0

            val fats = item.foodNutrients.find {
                it.nutrientId == 1004
            }?.value ?: 0.0

            val water = item.foodNutrients.find {
                it.nutrientId == 1051
            }?.value ?: 0.0

            val fiber = item.foodNutrients.find {
                it.nutrientId == 1079
            }?.value ?: 0.0

            val sugar = item.foodNutrients.find {
                it.nutrientId == 2000
            }?.value ?: 0.0

            val salt = item.foodNutrients.find {
                it.nutrientId == 1093
            }?.value ?: 0.0

            holder.bind(
                item.description,
                calories,
                proteins,
                carbs,
                fats,
                water,
                fiber,
                sugar,
                salt,
                context
            )

        }
    }
}