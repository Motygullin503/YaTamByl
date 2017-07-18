package ru.ksu.motygullin.yatambyl.entites;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopModel {

    @SerializedName("tracks")
    @Expose
    private List<Track> tracks = null;
    @SerializedName("users")
    @Expose
    private List<User> users = null;
    @SerializedName("likes")
    @Expose
    private List<Boolean> likes = null;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Boolean> getLikes() {
        return likes;
    }

    public void setLikes(List<Boolean> likes) {
        this.likes = likes;
    }

}