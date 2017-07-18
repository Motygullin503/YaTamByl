package ru.ksu.motygullin.yatambyl.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Api {
    private static final Api ourInstance = new Api();

    private YaTamBylAPI api;

    public static Api getInstance() {
        return ourInstance;
    }

    private Api() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kurs-work.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(YaTamBylAPI.class);
    }

    public YaTamBylAPI getApi() {
        return api;
    }

}
