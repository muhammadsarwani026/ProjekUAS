package com.example.muhammadsarwani.afinal;

/**
 * Created by Muhammad Sarwani on 12/29/2016.
 */

public class User {
    String name, username, password;

    public User(String name, String username, String password){
        this.name=name;
        this.username=username;
        this.password=password;
    }
    public User(String username, String password){
        this.username=username;
        this.password=password;
        this.name="";
    }
}
