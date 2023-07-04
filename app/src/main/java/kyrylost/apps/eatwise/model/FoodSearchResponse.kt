package kyrylost.apps.eatwise.model

import com.google.gson.annotations.SerializedName

data class FoodSearchResponse(
    val aggregations: Aggregations,
    val currentPage: Int,
    val foodSearchCriteria: FoodSearchCriteria,
    val foods: List<Food>,
    val pageList: List<Int>,
    val totalHits: Int,
    val totalPages: Int
) {
    data class Aggregations(
        val dataType: DataType,
        val nutrients: Nutrients
    ) {
        data class DataType(
            @SerializedName("Branded")
            val branded: Int,
            @SerializedName("Survey (FNDDS)")
            val survey: Int
        )

        class Nutrients
    }

    data class FoodSearchCriteria(
        val generalSearchInput: String,
        val numberOfResultsPerPage: Int,
        val pageNumber: Int,
        val pageSize: Int,
        val query: String,
        val requireAllWords: Boolean
    )

    data class Food(
        val allHighlightFields: String,
        val brandName: String,
        val brandOwner: String,
        val dataSource: String,
        val dataType: String,
        val description: String,
        val fdcId: Int,
        val finalFoodInputFoods: List<Any>,
        val foodAttributeTypes: List<Any>,
        val foodAttributes: List<Any>,
        val foodCategory: String,
        val foodMeasures: List<Any>,
        val foodNutrients: List<FoodNutrient>,
        val foodVersionIds: List<Any>,
        val gpcClassCode: Int,
        val gtinUpc: String,
        val householdServingFullText: String,
        val ingredients: String,
        val marketCountry: String,
        val microbes: List<Any>,
        val modifiedDate: String,
        val packageWeight: String,
        val preparationStateCode: String,
        val publishedDate: String,
        val score: Double,
        val servingSize: Double,
        val servingSizeUnit: String,
        val shortDescription: String,
        val tradeChannels: List<String>
    ) {
        data class FoodNutrient(
            val derivationCode: String,
            val derivationDescription: String,
            val derivationId: Int,
            val foodNutrientId: Int,
            val foodNutrientSourceCode: String,
            val foodNutrientSourceDescription: String,
            val foodNutrientSourceId: Int,
            val indentLevel: Int,
            val nutrientId: Int,
            val nutrientName: String,
            val nutrientNumber: String,
            val percentDailyValue: Int?,
            val rank: Int,
            val unitName: String,
            val value: Double
        )
    }
}