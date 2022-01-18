package com.crm.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.crm.app.model.Chat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PreferenceUtil {

    private static SharedPreferences sharedPreferencesToken;
    private static String APP_DATA_KEY = "FlutterSharedPreferences";
    private static String WHATSAPP_STATE = "WHATSAPP_STATE";



    public static void setContext(Context context) {
        sharedPreferencesToken = context.getSharedPreferences(APP_DATA_KEY, Context.MODE_PRIVATE);
    }

    public static void setPrevPackage(String token) {
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putString(WHATSAPP_STATE, token);
        editor.commit();
    }

    public static String getLastIncomingMessage(){
        return sharedPreferencesToken.getString("flutter.icomingChat", "");
    }

    public static void setLastIncomingMessage(String chat){
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putString("flutter.icomingChat", chat);
        editor.commit();
    }

    public static void setLastCount(int count){
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putInt("flutter.lastCount", count);
        editor.commit();
    }

    public static int getLastCount(){
        return sharedPreferencesToken.getInt("flutter.lastCount", 0);
    }

    public static String getPrevPackage() {
        return sharedPreferencesToken.getString(WHATSAPP_STATE, "");
    }




    public static String getPermissionStatus() {
        return sharedPreferencesToken.getString("flutter.permission_status", "");
    }

    public static void setChat(Chat chat) {
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();

        if(chat==null){
            editor.putString("UserData", "");
        }else{
            Gson gson = new Gson();
            String json = gson.toJson(chat);
            editor.putString("UserData", json);
        }

        editor.commit();
    }

    public static boolean isChatEmpty(){
        String chat = sharedPreferencesToken.getString("UserData", "");
        if(chat.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public static List<Chat> getListChat(){
        String listChat = sharedPreferencesToken.getString("flutter.listChat", "");
        if(listChat.equals("")){
            return new ArrayList<>();
        }else{
            Type listType = new TypeToken<ArrayList<Chat>>(){}.getType();
            List<Chat> chatList = new Gson().fromJson(listChat, listType);
            return chatList;
        }
    }

    public static void setListChat(List<Chat> chats){
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        String json = new Gson().toJson(chats );
        editor.putString("flutter.listChat", json);
        editor.commit();
    }

    public static void setMemberDeviceId(int id){
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putInt("flutter.memberDeviceId", id);
        editor.commit();
    }

    public static int getMemberDevice(){
        return sharedPreferencesToken.getInt("flutter.memberDeviceId", 0);
    }

    public static Chat getChat() {
        Gson gson = new Gson();
        String json = sharedPreferencesToken.getString("UserData", "");
        if(json.isEmpty()){
            return new Chat();
        }else{
            Chat chat = gson.fromJson(json, Chat.class);
            return chat;
        }
    }




    //Telegram
    public static Chat getTelegramChat() {
        Gson gson = new Gson();
        String json = sharedPreferencesToken.getString("telegram_chat", "");
        if(json.isEmpty()){
            return new Chat();
        }else{
            Chat chat = gson.fromJson(json, Chat.class);
            return chat;
        }
    }

    public static void setTelegramChat(Chat chat) {
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();

        if(chat==null){
            editor.putString("telegram_chat", "");
        }else{
            Gson gson = new Gson();
            String json = gson.toJson(chat);
            editor.putString("telegram_chat", json);
        }

        editor.commit();
    }

    public static String getTeleLastIncomingMessage(){
        return sharedPreferencesToken.getString("teleIcomingChat", "");
    }

    public static void setTeleLastIncomingMessage(String chat){
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putString("teleIcomingChat", chat);
        editor.commit();
    }
    public static void setTeleLastCount(int count){
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putInt("teleLastCount", count);
        editor.commit();
    }

    public static int getTeleLastCount(){
        return sharedPreferencesToken.getInt("teleLastCount", 0);
    }
}
