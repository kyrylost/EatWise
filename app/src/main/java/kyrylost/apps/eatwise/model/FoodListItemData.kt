package kyrylost.apps.eatwise.model

data class FoodListItemData(
    val description: String,
    val calories: Double,
    val proteins: Double,
    val carbs: Double,
    val fats: Double,
    val water: Double,
    val fiber: Double,
    val sugar: Double,
    val salt: Double
)
