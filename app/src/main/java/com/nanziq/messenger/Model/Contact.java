package com.nanziq.messenger.Model;

import java.util.List;

/**
 * Created by Konstantin on 08.08.2017.
 */

public class Contact {
    public String uid;
    public String name;
    public String phone;
    public String image;

    public Contact(){

    }

    public Contact(String uid, String name, String phone,  String image){
        this.uid = uid;
        this.name = name;
        this.phone = phone;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
