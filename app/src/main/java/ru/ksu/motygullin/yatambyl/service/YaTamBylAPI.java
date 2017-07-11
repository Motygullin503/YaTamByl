package ru.ksu.motygullin.yatambyl.service;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.ksu.motygullin.yatambyl.entites.PostModel;

public interface YaTamBylAPI {
    @POST("/users/add")
    Call<PostModel> addUsers(@Query("hash") String hash, @Query("name") String name);

}
