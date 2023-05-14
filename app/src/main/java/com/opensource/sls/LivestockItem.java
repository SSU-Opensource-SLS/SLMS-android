package com.opensource.sls;

import java.io.Serializable;

public class LivestockItem implements Serializable {
    Long id;
    String uid;
    String name;
    String type;
    String cattle;
    Boolean is_pregnancy;

    public LivestockItem() {
    }

    public LivestockItem(Long id, String uid, String name, String type, String cattle, Boolean is_pregnancy) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.type = type;
        this.cattle = cattle;
        this.is_pregnancy = is_pregnancy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCattle() {
        return cattle;
    }

    public void setCattle(String cattle) {
        this.cattle = cattle;
    }

    public Boolean getIs_pregnancy() {
        return is_pregnancy;
    }

    public void setIs_pregnancy(Boolean is_pregnancy) {
        this.is_pregnancy = is_pregnancy;
    }
}
