package com.opensource.sls;

import java.io.Serializable;

public class LivestockItem implements Serializable {
    String uid;
    String livestock_type;
    Long num;
    String name;
    String cattle;
    Number is_pregnancy;

    public LivestockItem() {
    }

    public LivestockItem(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public LivestockItem(String uid, String livestock_type, Long num, String name, String cattle, Number is_pregnancy) {
        this.uid = uid;
        this.livestock_type = livestock_type;
        this.num = num;
        this.name = name;
        this.cattle = cattle;
        this.is_pregnancy = is_pregnancy;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCattle() {
        return cattle;
    }

    public void setCattle(String cattle) {
        this.cattle = cattle;
    }

    public Number getIs_pregnancy() {
        return is_pregnancy;
    }

    public void setIs_pregnancy(Number is_pregnancy) {
        this.is_pregnancy = is_pregnancy;
    }
}
