package com.crm.app.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class PermissionStatus {
    @SerializedName("permissionStatus")
    @Getter @Setter
    private Permision permision;

    public PermissionStatus(boolean call, boolean sms, boolean wa, boolean phone, boolean gallery, boolean location){
       this.permision = new Permision(call, sms, wa, phone, gallery, location);
    }

    public class Permision{
        @SerializedName("wa")
        @Getter @Setter
        private boolean wa;
        @SerializedName("sms")
        @Getter @Setter
        private boolean sms;
        @SerializedName("location")
        @Getter @Setter
        private boolean location;
        @SerializedName("phone")
        @Getter @Setter
        private boolean phone;
        @SerializedName("call")
        @Getter @Setter
        private boolean call;
        @SerializedName("gallery")
        @Getter @Setter
        private boolean gallery;
        public Permision(boolean call, boolean sms, boolean wa, boolean phone, boolean gallery, boolean location){
            this.call = call;
            this.sms = sms;
            this.wa = wa;
            this.phone = phone;
            this.gallery = gallery;
            this.location = location;
        }
    }
}
