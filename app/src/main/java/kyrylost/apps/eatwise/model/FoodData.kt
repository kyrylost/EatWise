package kyrylost.apps.eatwise.model

data class FoodData(
    val description: String = "",
    val calories: Double = 0.0,
    val water: Double = 0.0,
    val proteins: Double = 0.0,
    val carbs: Double = 0.0,
    val fats: Double = 0.0,
    val fiber: Double = 0.0,
    val sugar: Double = 0.0,
    val salt: Double = 0.0
)
