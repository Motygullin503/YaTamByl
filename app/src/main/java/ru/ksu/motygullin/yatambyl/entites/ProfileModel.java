package ru.ksu.motygullin.yatambyl.entites;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModel {

    @SerializedName("tracks")
    @Expose
    private List<Track> tracks = null;
    @SerializedName("artworks")
    @Expose
    private List<Artwork> artworks = null;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }

}