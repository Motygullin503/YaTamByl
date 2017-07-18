package ru.ksu.motygullin.yatambyl.entites;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User {

    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("name")
    @Expose
    private String name;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
