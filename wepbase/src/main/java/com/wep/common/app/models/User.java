package com.wep.common.app.models;

/**
 * Created by PriyabratP on 19-08-2016.
 */
public class User {
    private int id;
    private String userName;
    private String userMobile;
    private String userDesignation;
    private String userRole;
    private String userLogin;
    private String userPassword;
    private String userAdhar;
    private String userEmail;
    private String userAddress;
    private String userFileLoc;

    public User() {
    }

    public User(String userName, String userMobile, String userDesignation, String userRole, String userLogin, String userPassword, String userAdhar, String userEmail, String userAddress, String userFileLoc) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userDesignation = userDesignation;
        this.userRole = userRole;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userAdhar = userAdhar;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.userFileLoc = userFileLoc;
    }

    public User(int id, String userName, String userMobile, String userDesignation, String userRole, String userLogin, String userPassword, String userAdhar, String userEmail, String userAddress, String userFileLoc) {
        this.id = id;
        this.userName = userName;
        this.userMobile = userMobile;
        this.userDesignation = userDesignation;
        this.userRole = userRole;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userAdhar = userAdhar;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.userFileLoc = userFileLoc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAdhar() {
        return userAdhar;
    }

    public void setUserAdhar(String userAdhar) {
        this.userAdhar = userAdhar;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserFileLoc() {
        return userFileLoc;
    }

    public void setUserFileLoc(String userFileLoc) {
        this.userFileLoc = userFileLoc;
    }
}
