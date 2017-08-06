package com.nanziq.messenger.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Konstantin on 06.08.2017.
 */

public class Dialog {
    public String name;
    public String title;
    public String image;
    public Date date;
    public String text;

    public Dialog(String name, String title, String image, Date date, String text){
        this.name = name;
        this.title = title;
        this.image = image;
        this.date = date;
        this.text = text;
    }

    public String getDateFormatted(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM\nHH.mm");
        String dateString = formatter.format(date);
        return dateString;

    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
