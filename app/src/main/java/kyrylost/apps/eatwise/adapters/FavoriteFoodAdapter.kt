package kyrylost.apps.eatwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kyrylost.apps.eatwise.databinding.FoodItemBinding
import kyrylost.apps.eatwise.model.FavoriteFood
import kyrylost.apps.eatwise.model.FoodData
import kyrylost.apps.eatwise.viewholders.FoodViewHolder
import java.util.*

class FavoriteFoodAdapter(private val favoriteFoodList: LinkedList<FavoriteFood>,
                          private val context: Context) :
    RecyclerView.Adapter<FoodViewHolder>() {

    var onItemClick : ((FoodData) -> Unit)? = null
    var onAddToFavoriteClick: ((FavoriteFood) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            FoodItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = favoriteFoodList[position]
        holder.bind(foodItem.foodData, context)

        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(foodItem.foodData)
        }

        holder.binding.addToFavorite.setOnClickListener {
            onAddToFavoriteClick?.invoke(foodItem)
            favoriteFoodList.remove(foodItem)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int = favoriteFoodList.size
}