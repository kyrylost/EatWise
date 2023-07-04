package kyrylost.apps.eatwise.viewholders

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import kyrylost.apps.eatwise.R
import kyrylost.apps.eatwise.databinding.FoodItemBinding

class FoodViewHolder(
    val binding: FoodItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(description: String, calories: Double,
             protein: Double, carbs: Double,
             fat: Double, water: Double,
             fiber: Double, sugar: Double,
             salt: Double, context: Context
    ) {
        binding.name.text = context.getString(R.string.food_list_desc, description)
        binding.cal.text = context.getString(R.string.food_list_calories, calories)
        binding.protein.text = context.getString(R.string.food_list_protein, protein)
        binding.carbs.text = context.getString(R.string.food_list_carbs, carbs)
        binding.fat.text = context.getString(R.string.food_list_fat, fat)
        binding.water.text = context.getString(R.string.food_list_water, water)
        binding.fiber.text = context.getString(R.string.food_list_fiber, fiber)
        binding.sugar.text = context.getString(R.string.food_list_sugar, sugar)
        binding.salt.text = context.getString(R.string.food_list_salt, salt)
    }

}