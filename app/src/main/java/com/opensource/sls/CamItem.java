package com.opensource.sls;

import java.io.Serializable;

public class CamItem implements Serializable {
    String uid;
    String livestock_type;
    Long num;
    String livestock_name;
    String url;

    public CamItem() {
    }

    public CamItem(String uid, String livestock_type, Long num, String livestock_name, String url) {
        this.uid = uid;
        this.livestock_type = livestock_type;
        this.num = num;
        this.livestock_name = livestock_name;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLivestock_type() {
        return livestock_type;
    }

    public void setLivestock_type(String livestock_type) {
        this.livestock_type = livestock_type;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getLivestock_name() {
        return livestock_name;
    }

    public void setLivestock_name(String livestock_name) {
        this.livestock_name = livestock_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
