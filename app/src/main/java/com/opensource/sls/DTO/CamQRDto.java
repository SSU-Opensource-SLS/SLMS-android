package com.opensource.sls.DTO;

import java.io.Serializable;

public class CamQRDto implements Serializable {
    String uid;
    String wifi_name;
    String wifi_pwd;
    String livestock_type;

    public CamQRDto() {
    }

    public CamQRDto(String uid, String wifi_name, String wifi_pwd, String livestock_type) {
        this.uid = uid;
        this.wifi_name = wifi_name;
        this.wifi_pwd = wifi_pwd;
        this.livestock_type = livestock_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWifi_name() {
        return wifi_name;
    }

    public void setWifi_name(String wifi_name) {
        this.wifi_name = wifi_name;
    }

    public String getWifi_pwd() {
        return wifi_pwd;
    }

    public void setWifi_pwd(String wifi_pwd) {
        this.wifi_pwd = wifi_pwd;
    }

    public String getLivestock_type() {
        return livestock_type;
    }

    public void setLivestock_type(String livestock_type) {
        this.livestock_type = livestock_type;
    }
}
