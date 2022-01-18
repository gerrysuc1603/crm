package com.crm.app.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class BaseModel<T> {
    @SerializedName("items")
    @Getter @Setter
    private List<T> items;
}
