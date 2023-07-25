package kyrylost.apps.eatwise.reseter

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.*
import java.util.concurrent.TimeUnit

// Schedule the task to run daily at the specific time
fun scheduleDatabaseResetAtSpecificTime(context: Context) {
    val currentTime = Calendar.getInstance()
    val scheduledTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 2)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    if (currentTime.after(scheduledTime)) {
        // If the specified time has already passed today, schedule it for tomorrow
        scheduledTime.add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = scheduledTime.timeInMillis - currentTime.timeInMillis

    val repeatingRequest = OneTimeWorkRequest.Builder(ResetDatabaseWorker::class.java)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()


    WorkManager.getInstance(context).enqueue(repeatingRequest)
}