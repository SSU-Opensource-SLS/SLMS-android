package com.opensource.sls;

public class UserModel {
    private String uid;
    private String name;
    private String token;
    private String email;
    private String birth;

    public UserModel() {}

    public UserModel(String uid, String name, String token, String email, String birth) {
        this.uid = uid;
        this.name = name;
        this.token = token;
        this.email = email;
        this.birth = birth;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
