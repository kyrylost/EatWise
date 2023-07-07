package kyrylost.apps.eatwise.viewmodel

import android.util.Log
import androidx.databinding.ObservableDouble
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kyrylost.apps.eatwise.model.ConsumedNutrients
import kyrylost.apps.eatwise.model.User
import kyrylost.apps.eatwise.room.ConsumedNutrientsRepository
import kyrylost.apps.eatwise.room.UserRepository
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ConsumedNutrientsViewModel @Inject constructor(
    private val consumedNutrientsRepository: ConsumedNutrientsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    lateinit var user: User

    var recommendedWater = ObservableDouble()

    private var activityCoefficient = 0.0
    var recommendedCalories = ObservableDouble()

    private var proteinsCoefficient = 0.0
    var recommendedProteins = ObservableDouble()

    private var carbsCoefficient = 0.0
    var recommendedCarbs = ObservableDouble()

    private var fatsCoefficient = 0.0
    var recommendedFats = ObservableDouble()

    val recommendedFiber = 30.0
    var recommendedSugar = ObservableDouble()
    val recommendedSalt = 2.3

    var consumedCalories = ObservableDouble(0.0)
    var consumedWater = ObservableDouble(0.0)
    var consumedProteins = ObservableDouble(0.0)
    var consumedCarbs = ObservableDouble(0.0)
    var consumedFats = ObservableDouble(0.0)
    var consumedFiber = ObservableDouble(0.0)
    var consumedSugar = ObservableDouble(0.0)
    var consumedSalt = ObservableDouble(0.0)

    fun calculateRecommendedNutrients() {
        CoroutineScope(Dispatchers.IO).launch {
            user = userRepository.getUser()!!

            Log.d("ConsNVM", user.toString())

            val userBirthDate = user.birthDate
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val parsedUserBirthDate = LocalDate.parse(userBirthDate, formatter)
            val currentDate = LocalDate.now()

            val period = Period.between(parsedUserBirthDate, currentDate)
            val userAge = period.years

            val userWeight = user.weight
            val userSex = user.sex
            val userWork = user.work
            val userTrainings = user.trainings
            val userDiet = user.diet

            activityCoefficient = if(userWork == 0) { // Active
                when(userTrainings) {
                    0 -> 1.5
                    in 1 .. 2 -> 1.7
                    else -> {1.9}
                }
            } else { // Sedentary
                when(userTrainings) {
                    0 -> 1.1
                    in 1..2 -> 1.3
                    in 3..4 -> 1.5
                    in 5..6 -> 1.7
                    else -> {1.9}
                }
            }

            recommendedCalories.set(
                if(userSex == 0) { // Male
                    recommendedSugar.set(36.0)
                    when (userAge) {
                        in 0..30 -> ((0.0630 * userWeight + 2.8957) * 240)
                        in 31..60 -> ((0.0491 * userWeight + 2.4587) * 240)
                        in 61..99 -> ((0.0491 * userWeight + 1.8988) * 240)
                        else -> 0.0
                    }
                } else { // Female
                    recommendedSugar.set(24.0)
                    when(userAge) {
                        in 0..30 -> ((0.0621 * userWeight + 2.0357) * 240)
                        in 31..60 -> ((0.0342 * userWeight + 3.5377) * 240)
                        in 61..99 -> ((0.0377 * userWeight + 2.7545) * 240)
                        else -> 0.0
                    }
                }
            )

            recommendedCalories.set(recommendedCalories.get() * activityCoefficient)

            recommendedWater.set(userWeight * 0.033)

            when(userDiet) {
                // Maintenance
                0 -> {
                    proteinsCoefficient = 0.2
                    fatsCoefficient = 0.2
                    carbsCoefficient = 0.6
                }
                // Gain
                1 -> {
                    recommendedCalories.set(recommendedCalories.get() * 1.1)
                    proteinsCoefficient = 0.28
                    fatsCoefficient = 0.2
                    carbsCoefficient = 0.52
                }
                // Lose
                2 -> {
                    recommendedCalories.set(recommendedCalories.get() * 0.8)
                    proteinsCoefficient = 0.31
                    fatsCoefficient = 0.29
                    carbsCoefficient = 0.41
                }
                // Cutting
                3 -> {
                    recommendedCalories.set(recommendedCalories.get() * 0.85)
                    proteinsCoefficient = 0.5
                    fatsCoefficient = 0.2
                    carbsCoefficient = 0.3
                }
            }

            recommendedProteins.set((recommendedCalories.get() * proteinsCoefficient) / 4)
            recommendedCarbs.set((recommendedCalories.get() * carbsCoefficient) / 4)
            recommendedFats.set((recommendedCalories.get() * fatsCoefficient) / 9)

            Log.d("calc", "$recommendedCalories $recommendedWater $recommendedProteins")
        }
    }

    fun getConsumedNutrients() {
        CoroutineScope(Dispatchers.IO).launch {
            var consumedNutrients = consumedNutrientsRepository.getConsumedNutrients()
            if (consumedNutrients == null) {

                consumedNutrientsRepository.insertConsumedNutrients(ConsumedNutrients()).apply {
                    consumedNutrients = consumedNutrientsRepository.getConsumedNutrients()?.apply {
                        consumedCalories.set(calories)
                        consumedWater.set(water)
                        consumedProteins.set(proteins)
                        consumedCarbs.set(carbs)
                        consumedFats.set(fats)
                        consumedFiber.set(fiber)
                        consumedSugar.set(sugar)
                        consumedSalt.set(salt)
                    }
                }

            } else {

                consumedCalories.set(consumedNutrients!!.calories)
                consumedWater.set(consumedNutrients!!.water)
                consumedProteins.set(consumedNutrients!!.proteins)
                consumedCarbs.set(consumedNutrients!!.carbs)
                consumedFats.set(consumedNutrients!!.fats)
                consumedFiber.set(consumedNutrients!!.fiber)
                consumedSugar.set(consumedNutrients!!.sugar)
                consumedSalt.set(consumedNutrients!!.salt)

            }
        }
    }



}