package com.crm.app;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

class NotificationObject {

    private Context context;
    private Notification notification;

    private String packageName;
    private long postTime;
    private long systemTime;
    private boolean isOngoing;

    private long when;
    private String appName;
    private String title;
    private String text;
    private String extraText;
    private String textBig;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public NotificationObject(StatusBarNotification sbn, Context context) {

        this.context = context;
        notification = sbn.getNotification();
        packageName = sbn.getPackageName();
        postTime = notification.when;
        systemTime = System.currentTimeMillis();
        isOngoing = sbn.isOngoing();
        when = notification.when;


        Bundle extras = NotificationCompat.getExtras(notification);
        appName = utils.getAppNameFromPackage(context, packageName, false);

        if (extras != null) {
            title = utils.nullToEmptyString(extras.getCharSequence(NotificationCompat.EXTRA_TITLE));
            text = utils.nullToEmptyString(extras.getCharSequence(NotificationCompat.EXTRA_TEXT));
            extraText = utils.nullToEmptyString(extras.getCharSequence(Notification.EXTRA_SUB_TEXT));
            textBig = utils.nullToEmptyString(extras.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT));

//            Log.v("NotifObject","title :" + title +"\n"
//                     + "text :" + text + "\n"
//            +"extraText: " + extraText + "\n"
//            +"textBig: " + textBig);
            int textlen = text.length();
            if (extraText.length() != 0)
                text += "\n" + extraText;

            if(textBig.length() != 0){
//                Log.v("NotifObject","index: " +index + "   Modified extra text :" + textBig.substring(textlen + 1));
                text += textBig.substring(textlen);
            }


        }

    }

    public long getPostTime() {
        return postTime;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public String getAppName() {
        return appName;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean getisOngoing(){
        return isOngoing;
    }
}
