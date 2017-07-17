package ru.ksu.motygullin.yatambyl.service;

import android.content.Intent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.ksu.motygullin.yatambyl.entites.Artwork;
import ru.ksu.motygullin.yatambyl.entites.PostModel;

public interface YaTamBylAPI {
    @POST("/users/add")
    Call<PostModel> addUsers(@Query("name") String name, @Query("hash") String hash);

    @GET("/artworks/get_by_id")
    Call<Artwork> getArtworkById (@Query("id") Integer id);

    @GET("/artworks/get_by_date")
    Call<Artwork> getArtworkByDate (@Query("date") String date);

    @POST("/tracks/add")
    Call<PostModel> addTracks (@Query("user_id") String userId, @Query("artwork_id") Integer artworkId, @Query("link") String link);

    @GET("artworks/get_by_user_id/today")
    Call<Artwork> getTaskForToday (@Query("id") String uid);

}
