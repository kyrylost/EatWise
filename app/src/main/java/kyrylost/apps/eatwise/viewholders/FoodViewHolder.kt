package kyrylost.apps.eatwise.viewholders

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import kyrylost.apps.eatwise.R
import kyrylost.apps.eatwise.databinding.FoodItemBinding
import kyrylost.apps.eatwise.model.FoodData

class FoodViewHolder(
    val binding: FoodItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(itemData: FoodData,
             context: Context
    ) {
        binding.name.text = context.getString(R.string.food_list_desc, itemData.description)
        binding.cal.text = context.getString(R.string.food_list_calories, itemData.calories)
        binding.protein.text = context.getString(R.string.food_list_protein, itemData.proteins)
        binding.carbs.text = context.getString(R.string.food_list_carbs, itemData.carbs)
        binding.fat.text = context.getString(R.string.food_list_fat, itemData.fats)
        binding.water.text = context.getString(R.string.food_list_water, itemData.water)
        binding.fiber.text = context.getString(R.string.food_list_fiber, itemData.fiber)
        binding.sugar.text = context.getString(R.string.food_list_sugar, itemData.sugar)
        binding.salt.text = context.getString(R.string.food_list_salt, itemData.salt)
    }
}