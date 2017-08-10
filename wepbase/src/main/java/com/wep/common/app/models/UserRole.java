package com.wep.common.app.models;

/**
 * Created by PriyabratP on 18-08-2016.
 */
public class UserRole {

    int id;
    String role;

    public UserRole(){
    }

    public UserRole(int id) {
        this.id = id;
    }
    public UserRole(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
