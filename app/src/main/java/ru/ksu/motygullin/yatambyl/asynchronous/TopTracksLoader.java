package ru.ksu.motygullin.yatambyl.asynchronous;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.entites.TopModel;
import ru.ksu.motygullin.yatambyl.service.Api;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;


public class TopTracksLoader extends AsyncTaskLoader<TopModel> {

    public TopTracksLoader(Context context) {
        super(context);
        forceLoad();
    }

    @Override
    public TopModel loadInBackground() {

        TopModel model = null;

        YaTamBylAPI api = Api.getInstance().getApi();
        Call<TopModel> call = api.getTopTracks(FirebaseAuth.getInstance().getCurrentUser().getUid());

        try {
            Response<TopModel> response = call.execute();
            model = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }
}
