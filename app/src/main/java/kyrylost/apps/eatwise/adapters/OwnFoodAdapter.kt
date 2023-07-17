package kyrylost.apps.eatwise.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kyrylost.apps.eatwise.databinding.FoodItemBinding
import kyrylost.apps.eatwise.model.FoodData
import kyrylost.apps.eatwise.model.OwnFood
import kyrylost.apps.eatwise.viewholders.FoodViewHolder
import java.util.*

class OwnFoodAdapter(private val ownFoodList: LinkedList<OwnFood>,
                     private val context: Context
): RecyclerView.Adapter<FoodViewHolder>() {

    var onItemClick : ((FoodData) -> Unit)? = null
    var onDeleteClick: ((OwnFood) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            FoodItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = ownFoodList[position]
        holder.bind(foodItem.foodData, context)

        holder.binding.addToFavorite.isVisible = false
        holder.binding.deleteOwnFood.isVisible = true

        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(foodItem.foodData)
        }

        holder.binding.deleteOwnFood.setOnClickListener {
            onDeleteClick?.invoke(foodItem)
            ownFoodList.remove(foodItem)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }

    }

    override fun getItemCount(): Int = ownFoodList.size

    fun addNewElement(ownFood: OwnFood) {
        ownFoodList.add(ownFood)
        notifyItemInserted(itemCount)
    }

}