package com.example.mocom_project2;

public class User {
    private String userID;
    private String userPassword;
    private String userName;

    public User(String userID, String userPassword, String userName) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
    }


    public String getEmail() {
        return userID;
    }

    public String getPassword() {
        return userPassword;
    }
    public String getUserName() {
        return userName;
    }

}
