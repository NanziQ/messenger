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
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH.mm");
        String dateString = formatter.format(date);
        return dateString;

    }
}
