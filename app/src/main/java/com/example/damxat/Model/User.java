package com.example.damxat.Model;


public class User {
    String id;
    String username;
    String status;

    //constructor de User amb tots els paràmetres
    public User(String id, String username, String status) {
        this.id = id;
        this.username = username;
    }
//constructor buit
    public User() {
    }
//getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus(){
        return status;
    }
//setter de estat
    public void setStatus(String status) {
        this.status = status;
    }
}
