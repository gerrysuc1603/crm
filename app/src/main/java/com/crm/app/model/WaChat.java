package com.crm.app.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class WaChat {
    @SerializedName("memberDeviceId")
    @Getter @Setter
    private int memberDeviceId;
    @SerializedName("time")
    @Getter @Setter
    private String time;
    @SerializedName("type")
    @Getter @Setter
    private String type;
    @SerializedName("phone")
    @Getter @Setter
    private String phone;
    @SerializedName("message")
    @Getter @Setter
    private String message;

    public WaChat(){

    }
}
