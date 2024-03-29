package kyrylost.apps.eatwise.reseter

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kyrylost.apps.eatwise.model.ConsumedNutrients
import kyrylost.apps.eatwise.room.consumednutrients.ConsumedNutrientsRepository

@HiltWorker
class ResetDatabaseWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: ConsumedNutrientsRepository,
    ) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val consumedNutrients = repository.getConsumedNutrients(1)
            repository.resetConsumedNutrients(ConsumedNutrients())
            if (consumedNutrients != null) {
                consumedNutrients.id = 2
                repository.insertConsumedNutrients(consumedNutrients)
            }
            scheduleDatabaseResetAtSpecificTime(applicationContext)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}