package com.example.androidlogin;

public class User {
    public String fName, lName, email, password,uniName;
    public String yearOfStudy;

    public User(){

    }

    public User(String fName, String lName, String email, String password, String uniName, String yearOfStudy){
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
        this.uniName = uniName;
        this.yearOfStudy = yearOfStudy;
    }

}
