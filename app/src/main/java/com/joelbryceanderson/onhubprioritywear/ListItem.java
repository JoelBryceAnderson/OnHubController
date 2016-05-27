package com.joelbryceanderson.onhubprioritywear;

import android.graphics.drawable.Drawable;

/**
 * Model class that holds the IFTTT Maker command, device name, and icon for each priority device.
 */
public class ListItem {

    private String name;
    private Drawable icon;
    private String command;

    public ListItem(String name, Drawable icon, String command) {
        this.name = name;
        this.icon = icon;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
