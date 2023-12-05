package kyrylost.apps.eatwise.reseter

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

// Schedule the task to run daily at the specific time
fun scheduleDatabaseResetAtSpecificTime(context: Context) {

    val taskScheduledSharedPreferences = context.getSharedPreferences(
        "isTaskScheduled", Context.MODE_PRIVATE
    ) ?: return

    val currentTime = Calendar.getInstance()
    val scheduledTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 3)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    if (currentTime.after(scheduledTime)) {
        // If the specified time has already passed today, schedule it for tomorrow
        scheduledTime.add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = scheduledTime.timeInMillis - currentTime.timeInMillis

    //The timer(1 day) starts after Initial Delay
//    val repeatingRequest = PeriodicWorkRequest.Builder(
//        ResetDatabaseWorker::class.java,
//        1,
//        TimeUnit.DAYS,
//        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
//        TimeUnit.MILLISECONDS)
//        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
//        .build()

  val repeatingRequest = OneTimeWorkRequest.Builder(ResetDatabaseWorker::class.java)
     .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
     .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "database_reset",
        ExistingWorkPolicy.REPLACE,
        repeatingRequest
    )

    taskScheduledSharedPreferences.edit().putBoolean("isTaskScheduled", true).apply()
}