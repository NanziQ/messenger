package com.nanziq.messenger.Model;

import java.util.List;

/**
 * Created by Konstantin on 08.08.2017.
 */

public class Contact {
    public String name;
    public String phone;
    public String image;
    public List<Long> dialogs;

    public Contact(){

    }

    public Contact(String name, String phone,  String image, List<Long> dialogs){
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.dialogs = dialogs;
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

    public void setDialogs(List<Long> dialogs) {
        this.dialogs = dialogs;
    }

    public List<Long> getDialogs() {
        return dialogs;
    }
}
