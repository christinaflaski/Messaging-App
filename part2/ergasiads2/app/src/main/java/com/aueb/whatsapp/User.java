package com.aueb.whatsapp;

import java.util.ArrayList;

public class User {
    public String name;
    public ArrayList<String> groupchats;
    public String port;
    public String ip;

    public User(String name,ArrayList<String> groupchats,String port,String ip){
        this.name=name;
        this.groupchats=groupchats;
        this.port=port;
        this.ip=ip;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
