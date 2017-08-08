package com.nanziq.messenger.Model;

/**
 * Created by Konstantin on 08.08.2017.
 */

public class Contact {
    public String name;
    public String image;
    public Contact(String name, String image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
