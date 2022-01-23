package com.crm.app;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;

import androidx.annotation.RequiresApi;

import com.crm.app.model.Chat;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    private final String TAG = NotificationListener.class.getSimpleName();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "ID:" + sbn.getId());
        Log.i(TAG, "Posted by:" + sbn.getPackageName());
        NotificationObject no = new NotificationObject(sbn, getApplicationContext());
        if(no.getText().length() == 0 ||
                no.getTitle().equals("WhatsApp Web") ||
                no.getTitle().equals("WhatsApp")||
                (!no.getAppName().equals("WhatsApp")&&
                !no.getPackageName().equals("org.telegram.messenger")&&
                !no.getPackageName().equals("com.skype.raider"))){
            return;
        }
        String appName = no.getAppName();
        String title = no.getTitle();
        String text = no.getText();
        String time = utils.getTime(no.getPostTime());
        String date = utils.getDate(no.getSystemTime());
        String packageName = no.getPackageName();
        Chat chat = new Chat();
        chat.setTime(time);
        chat.setMessage(text);
        chat.setName(title);
        chat.setType("Masuk");
        chat.setSource(no.getPackageName());
        if(!text.contains("new messages")){
            EventBus.getDefault().post(chat);
        }


//        try {
//            NotificationHandler notificationHandler = new NotificationHandler(this);
//            notificationHandler.handlePostedNotif(sbn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        Log.v(TAG, "ID:" + sbn.getId());
//        Log.v(TAG, "Removed ," + "Posted by:" + sbn.getPackageName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Connected");
    }

    @Override
    public void onListenerDisconnected (){
        Log.v(TAG,"Disconnected");
    }
}
