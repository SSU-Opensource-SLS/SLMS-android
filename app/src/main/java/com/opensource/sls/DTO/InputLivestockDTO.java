package com.opensource.sls.DTO;

public class InputLivestockDTO {
    String uid;
    String livestock_type;
    String name;
    String cattle;

    public InputLivestockDTO(String uid, String livestock_type, String name, String cattle) {
        this.uid = uid;
        this.livestock_type = livestock_type;
        this.name = name;
        this.cattle = cattle;
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
}
