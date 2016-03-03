package com.pitchedapps.material.glass.models;

import android.graphics.drawable.Drawable;

/**
 * Created by 7681 on 2016-02-24.
 */
public class HomeCard {

    public String title;
    public String desc;
    public Drawable img;
    public boolean imgEnabled;
    public String onClickLink;

    public HomeCard(Builder builder) {
        this.title = builder.title;
        this.desc = builder.desc;
        this.img = builder.img;
        this.imgEnabled = builder.imgEnabled;
        this.onClickLink = builder.onClickLink;
    }

    public static class Builder {
        private String title, desc;
        private Drawable img;
        private boolean imgEnabled = false;
        private String onClickLink;

        public Builder() {
            this.title = "Insert title here";
            this.desc = "Insert description here";
            this.img = null;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder icon(Drawable img) {
            this.img = img;
            this.imgEnabled = true;
            return this;
        }

        public Builder onClickLink (String s) {
            this.onClickLink = s;
            return this;
        }

        public HomeCard build() {
            return new HomeCard(this);
        }
    }
}