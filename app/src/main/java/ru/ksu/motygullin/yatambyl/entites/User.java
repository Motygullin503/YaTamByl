package ru.ksu.motygullin.yatambyl.entites;


import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {

    private String hash;
    private String username;
    private int rating;

    private Uri photo;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public User(String hash, String username, int rating, Uri photo) {
        this.hash = hash;
        this.username = username;
        this.rating = rating;
        this.photo = photo;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User &&
                (((User) obj).getUsername().equals(username))&&
                ((User) obj).getHash().equals(hash)&&
                ((User) obj).getRating() == rating &&
                ((User) obj).getPhoto().equals(photo);
    }

    @Override
    public String toString() {
        return username;
    }


}
