package com.example.damxat.Model;

import java.util.ArrayList;
//Classe XatGroup un nom i 2 llistes
public class XatGroup {
    String name;
    ArrayList<String> users;
    ArrayList<Xat> xats;

//constructor XatGroup amb nomès el nom
    public XatGroup(String name) {
        this.name = name;

    }
//Constructor buit
    public XatGroup() {
    }

//getters de tots els valors
    public String getName() {
        return name;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public ArrayList<Xat> getXats() {
        return xats;
    }
}
