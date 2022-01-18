package com.crm.app.base;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class BaseRequest {
    @SerializedName("data")
    @Getter @Setter
    private String data;
    public BaseRequest(){

    }
    public BaseRequest(String data){
        this.data = data;
    }
}
