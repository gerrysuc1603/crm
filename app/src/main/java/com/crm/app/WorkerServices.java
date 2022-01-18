package com.crm.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created on : Mar 26, 2019
 * Author     : AndroidWave
 */
public class WorkerServices extends Worker {
    private static final String WORK_RESULT = "work_result";
    public WorkerServices(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        //PermissionStatus json = new Gson().fromJson(PreferenceUtil.getPermissionStatus(), PermissionStatus.class);
        PreferenceUtil.setContext(getApplicationContext());
        Log.d("heheheh",PreferenceUtil.getPermissionStatus());
        String encrypt = utils.encrypt(PreferenceUtil.getPermissionStatus(), "3akUaH9PaRdTp45ZT1");

//        MonitorPresenter monitorPresenter = new MonitorPresenter(getApplicationContext());
//        BaseRequest baseRequest = new BaseRequest();
//        baseRequest.setData(encrypt.replace("\n",""));
//        monitorPresenter.getIndicator(baseRequest);
//
//        Data taskData = getInputData();
//        String taskDataString = taskData.getString(MainActivity.MESSAGE_STATUS);
//        showNotification("WorkManager", taskDataString != null ? taskDataString : "Message has been Sent");

        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        return Result.success(outputData);
    }
    private void showNotification(String task, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_channel";
        String channelName = "task_name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1, builder.build());
    }
}