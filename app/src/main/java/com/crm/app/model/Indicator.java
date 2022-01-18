package com.crm.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

public class Indicator {
    @SerializedName("requestedTypes")
    @Getter
    private List<String> requestedTypes;
    @SerializedName("memberDeviceId")
    @Getter
    private int memberDeviceId;
}
