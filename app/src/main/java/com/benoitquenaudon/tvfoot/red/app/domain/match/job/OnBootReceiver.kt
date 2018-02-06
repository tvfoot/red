package com.benoitquenaudon.tvfoot.red.app.domain.match.job

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.content.systemService
import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchNotificationSchedulerService.Companion.MATCH_NOTIFICATION_SCHEDULER_JOB_ID
import timber.log.Timber


/**
 * Alarms set via AlarmManager are deleted on reboot.
 * We set them again here.
 */
class OnBootReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    if (intent.action == "android.intent.action.BOOT_COMPLETED") {
      val jobInfo = JobInfo.Builder(MATCH_NOTIFICATION_SCHEDULER_JOB_ID,
          ComponentName(context, MatchNotificationSchedulerService::class.java))
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
          .build()

      context.systemService<JobScheduler>().schedule(jobInfo)
    } else {
      Timber.e("Intent's action is unknown: ${intent.action}")
    }
  }
}