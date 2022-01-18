package com.crm.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;


public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceUtil.setContext(context);
        Log.d("heheheh",PreferenceUtil.getPermissionStatus());
        String encrypt = utils.encrypt(PreferenceUtil.getPermissionStatus(), "3akUaH9PaRdTp45ZT1");

//        MonitorPresenter monitorPresenter = new MonitorPresenter(context);
//        BaseRequest baseRequest = new BaseRequest();
//        baseRequest.setData(encrypt.replace("\n",""));
//        monitorPresenter.getIndicator(baseRequest);
    }
}