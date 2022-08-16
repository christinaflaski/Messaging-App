package com.aueb.whatsapp;

import java.util.ArrayList;

public class UsersInfo {
    public User user1;
    public User user2;
    public User user3;
    private ArrayList<String> groupchats1=new ArrayList<>();
    private ArrayList<String> groupchats2=new ArrayList<>();
    private ArrayList<String> groupchats3=new ArrayList<>();

    public UsersInfo(){
        fillgcs();
        this.user1=new User("xristina",groupchats1,"300","192.168.56.1");
        this.user2=new User("giannis",groupchats2,"400","192.168.56.2");
        this.user3=new User("kleon",groupchats3,"500","192.168.56.3");

    }
    public void fillgcs(){
        groupchats1.add("class");
        groupchats1.add("cats");
        groupchats1.add("friends");
        groupchats1.add("football");
        groupchats1.add("tiktok");
        groupchats1.add("memes");
        groupchats2.add("tiktok");
        groupchats2.add("memes");
        groupchats2.add("gaming");
        groupchats2.add("class");
        groupchats2.add("cats");
        groupchats2.add("dogs");
        groupchats3.add("cats");
        groupchats3.add("friends");
        groupchats3.add("family");
        groupchats3.add("basketball");
        groupchats3.add("university");
        groupchats3.add("dogs");
    }
}
