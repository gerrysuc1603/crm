package com.crm.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.accessibilityservice.AccessibilityService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.crm.app.databinding.ActivityMainBinding;
import com.crm.app.model.Chat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    CardView accessibility_btn;
    CardView notification_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        initView();

    }
    private void initView(){
        accessibility_btn = activityMainBinding.accessibilityBtn;
        accessibility_btn.setOnClickListener(view -> {
            if (!isAccessibilityOn(MainActivity.this, MyAccessibilityService.class)) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);

            }
        });
        notification_btn = activityMainBinding.notificationBtn;
        notification_btn.setOnClickListener(view -> {
            if(!isNotificationServiceRunning()){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Enable Notfication Access").setTitle("Enable permissions");
                builder.setMessage("Enable Notfication Access or the notifications won't be Logged")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Enable Notification Access");
                alert.show();
            }
        });
    }
    private boolean isAccessibilityOn(Context context, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }
        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String accessibilityService = colonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    public boolean checkDuplicate(List<Chat> chats, Chat chat){
        boolean isDuplicate = false;
        for(int i=0;i<chats.size();i++){
            if(chats.get(i).getTime().equals(chat.getTime())){
                isDuplicate = true;
            }
        }
        return isDuplicate;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMachineList(Chat chat) {
        List<Chat> chats = PreferenceUtil.getListChat();
        if(checkDuplicate(chats, chat)){
            Log.d("asd","asd");
        }else {
            chats.add(chat);
        }
        PreferenceUtil.setListChat(chats);
//        Gson gson = new Gson();
//        String json = gson.toJson(chat);
//        MethodChannel channel = new MethodChannel(this.getFlutterEngine().getDartExecutor(), CHANNEL);
//        channel.invokeMethod("accessibility", json, new MethodChannel.Result() {
//            @Override
//            public void success(Object result) {
//                Log.d("testing", "success");
//            }
//
//            @Override
//            public void error(String errorCode, String errorMessage, Object errorDetails) {
//                Log.d("testing", "fail");
//
//            }
//
//            @Override
//            public void notImplemented() {
//                Log.d("testing", "not implemented");
//
//            }
//        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }
}