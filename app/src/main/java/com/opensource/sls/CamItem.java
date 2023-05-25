package com.opensource.sls;

public class CamItem {
    String uid;
    String livestock_type;
    Long num;

    public CamItem() {
    }

    public CamItem(String uid, String livestock_type, Long num) {
        this.uid = uid;
        this.livestock_type = livestock_type;
        this.num = num;
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
}
