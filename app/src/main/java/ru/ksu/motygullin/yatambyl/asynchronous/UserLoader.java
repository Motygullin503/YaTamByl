package ru.ksu.motygullin.yatambyl.asynchronous;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.entites.User;
import ru.ksu.motygullin.yatambyl.service.Api;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;

/**
 * Created by Bulat on 18.07.2017 at 11:13.
 */

public class UserLoader extends AsyncTaskLoader<User> {

    public UserLoader(Context context) {
        super(context);



        forceLoad();
    }

    @Override
    public User loadInBackground() {

        User user = null;
        YaTamBylAPI api = Api.getInstance().getApi();
        Call<User> call = api.getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid());

        try {
            Response<User> response = call.execute();
            user = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user != null) {
            Log.d("WARN", user.getName());
        } else {
            Log.e("WARN", "artwork is empty");
        }
        return user;
    }
}