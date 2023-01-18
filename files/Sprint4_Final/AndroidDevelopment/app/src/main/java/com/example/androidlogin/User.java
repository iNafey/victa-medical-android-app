package com.example.androidlogin;

//Author: Abdul Nafey Mohammed(anm30)

//User object class which will be pushed to firebase as part of the Users table.
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
