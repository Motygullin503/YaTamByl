package ru.ksu.motygullin.yatambyl.asynchronous;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.entites.ProfileModel;
import ru.ksu.motygullin.yatambyl.entites.Track;
import ru.ksu.motygullin.yatambyl.service.Api;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;


public class ProfileModelLoader extends AsyncTaskLoader<ProfileModel> {

    public ProfileModelLoader(Context context) {
        super(context);

        forceLoad();
    }

    @Override
    public ProfileModel loadInBackground() {

        ProfileModel model = null;
        YaTamBylAPI api = Api.getInstance().getApi();
        Call<ProfileModel> call = api.getAllRecordsById("cmaqxYCgFGcu6llseNH1KDWoOwA3");

        try {
            Response<ProfileModel> response = call.execute();
            model = response.body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (model != null) {
            for (Track track :model.getTracks()){
                Log.d("WARN", track.getLink());
            }

        } else {
            Log.e("WARN", "artwork is empty");
        }

        return model;
    }
}
