package com.example.mocom_project2;

public class Login {
    private String userID;
    private String userPassword;

    public Login(String userID, String userPassword) {
        this.userID = userID;
        this.userPassword = userPassword;
    }

    public String getEmail() {
        return userID;
    }

    public String getPassword() {
        return userPassword;
    }


}
