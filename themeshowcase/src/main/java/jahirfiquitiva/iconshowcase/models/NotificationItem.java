/*
 * Copyright (c) 2016. Jahir Fiquitiva. Android Developer. All rights reserved.
 */

package jahirfiquitiva.iconshowcase.models;

public class NotificationItem {

    private String text;
    private int type, ID;

    public NotificationItem(String text, int type, int ID) {
        this.text = text;
        this.type = type;
        this.ID = ID;
    }

    public String getText() {
        return text;
    }
    public int getType() {
        return type;
    }
    public int getID() {
        return ID;
    }
}
