package ru.ksu.motygullin.yatambyl.asynchronous;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.entites.Artwork;
import ru.ksu.motygullin.yatambyl.service.Api;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;


public class ArtworkLoader extends AsyncTaskLoader<Artwork> {

    public ArtworkLoader(Context context) {
        super(context);



        forceLoad();
    }

    @Override
    public Artwork loadInBackground() {

        Artwork artwork = null;
        YaTamBylAPI api = Api.getInstance().getApi();
        Call<Artwork> call = api.getTaskForToday(FirebaseAuth.getInstance().getCurrentUser().getUid());

        try {
            Response<Artwork> response = call.execute();
            artwork = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (artwork != null) {
            Log.d("WARN", artwork.getName());
        } else {
            Log.e("WARN", "artwork is empty");
        }
        return artwork;
    }
}
