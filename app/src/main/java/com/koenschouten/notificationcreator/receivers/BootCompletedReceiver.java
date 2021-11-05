package com.koenschouten.notificationcreator.receivers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.koenschouten.notificationcreator.services.OnBootJobService;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            ComponentName serviceComponent = new ComponentName(context, OnBootJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }
    }
}
