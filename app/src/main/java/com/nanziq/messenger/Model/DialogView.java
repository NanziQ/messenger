package com.nanziq.messenger.Model;

/**
 * Created by Konstantin on 23.08.2017.
 */

public class DialogView {
    private String name;
    private String image;
    private String text;

    public DialogView(){}

    public DialogView(String name, String image, String text) {
        this.name = name;
        this.image = image;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
