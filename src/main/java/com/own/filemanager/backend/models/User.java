package com.own.filemanager.backend.models;

public class User {
    private String userRole;

    public User(String role) {
        this.userRole = role;
    }

    public void setRole(String role) {
        this.userRole = role;
    }
    
    public String getRole() {
        return this.userRole;
    }
}
