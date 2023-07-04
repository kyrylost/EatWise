package kyrylost.apps.eatwise.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "birth_date") val birthDate: String,
    @ColumnInfo(name = "weight") val weight: Double,
    @ColumnInfo(name = "sex") val sex: Int, // 0 - male, 1 - female
    @ColumnInfo(name = "work") val work: Int, // 0 - active, 1 - sedentary
    @ColumnInfo(name = "trainings") val trainings: Int,
    @ColumnInfo(name = "diet") val diet: Int,
)
