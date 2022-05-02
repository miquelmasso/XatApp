package com.example.damxat.Model;

public class Xat {

    String sender;
    String receiver;
    String message;

    //constructor xat agafa per paràmetre de fora sender i message i ho atribueix a la classe xat
    public Xat(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

//constructor xat buit
    public Xat() {

    }

//constructor xat amb paràmetres sender, receiver i message
    public Xat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
//getters per poder objenir els valors
    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
