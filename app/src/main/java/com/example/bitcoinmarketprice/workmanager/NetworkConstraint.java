package com.example.bitcoinmarketprice.workmanager;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class NetworkConstraint {

    private final Context context;

    private NetworkConstraint(Context context) {
        this.context = context;
    }

    public static NetworkConstraint getInstance(Context context) {
        return new NetworkConstraint(context);
    }

    public void fetchData () {
        Constraints workConstraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SyncDataWorker.class, 1, TimeUnit.MINUTES)
                .setConstraints(workConstraint)
                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(periodicWorkRequest);
    }

    public void fetchDataOnce() {
        Constraints workConstraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SyncDataWorker.class)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .setConstraints(workConstraint)
                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(oneTimeWorkRequest);
    }

    public void expediteFetchData() {
        Constraints workConstraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SyncDataWorker.class)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(workConstraint)
                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(oneTimeWorkRequest);
    }
}
