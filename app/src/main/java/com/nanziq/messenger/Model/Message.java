package com.nanziq.messenger.Model;

/**
 * Created by Konstantin on 17.08.2017.
 */

public class Message {
    public String name;
    public String text;

    public Message(){}

    public Message(String name, String text){
        this.name = name;
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
