package com.crm.app.model;

import lombok.Getter;
import lombok.Setter;

public class Chat {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int memberDeviceId;
    @Getter @Setter
    private String time;
    @Getter @Setter
    private String message;
    @Getter @Setter
    private String type;
    @Getter @Setter
    private String source;
    @Getter @Setter
    private String parent;
    public Chat(){

    }
    public Chat(String name, String message){
        this.name = name;
        this.message = message;
    }
}
