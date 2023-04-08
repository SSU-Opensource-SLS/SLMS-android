package com.opensource.sls;

import java.util.ArrayList;

public class UserModel {
    private String name;
    private String email;
    private String birth;
    private ArrayList<String> livestocks;

    public UserModel() {}

    public UserModel(String name, String email, String birth, ArrayList<String> livestocks) {
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.livestocks = livestocks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public ArrayList<String> getLivestocks() {
        return livestocks;
    }

    public void setLivestocks(ArrayList<String> livestocks) {
        this.livestocks = livestocks;
    }
}
