package com.example.pc.myapplication;

/**
 * Created by pc on 12/14/2019.
 */

public class AddUserRead {
    public String username,email,pass;

    public AddUserRead() {
    }

    public AddUserRead(String username, String email, String pass) {
        //this.key = key;
        this.username = username;
        this.email = email;
        this.pass = pass;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
