package com.crm.app.base;

import android.content.Context;
import android.widget.Toast;

public class BasePresenter {
    public String STATUS_SUCCESS = "success";
    public String STATUS_ERROR = "error";
    public String STATUS_UNAUTHORIZED = "unauthorized";

    public void onConnectionFailure(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void onConnectionFailure(Context context){
        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    protected boolean isStatusSuccess(String status){
        return status.equals(STATUS_SUCCESS);
    }


}
