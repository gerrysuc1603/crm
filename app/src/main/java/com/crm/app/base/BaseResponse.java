package com.crm.app.base;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public class BaseResponse {
    @SerializedName("status")
    @Getter
    private String status;
    @SerializedName("message")
    @Getter
    private String message;
    @SerializedName("data")
    @Getter
    private String data;
}
