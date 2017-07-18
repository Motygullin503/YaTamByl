package ru.ksu.motygullin.yatambyl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.adapters.ArtworkRecyclerViewAdapter;
import ru.ksu.motygullin.yatambyl.asynchronous.ProfileModelLoader;
import ru.ksu.motygullin.yatambyl.entites.ProfileModel;

public class MyProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ProfileModel> {
    public static final int LOADER_ID = 222;
    TextView username;
    TextView userRating;
    TextView numOfTracks;
    ArtworkRecyclerViewAdapter adapter;

    RecyclerView tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        username = (TextView) findViewById(R.id.name);
        userRating = (TextView) findViewById(R.id.num_of_rating);
        numOfTracks = (TextView) findViewById(R.id.num_of_tracks);
        tracks = (RecyclerView) findViewById(R.id.tracks);


        adapter = new ArtworkRecyclerViewAdapter(null);
        tracks.setLayoutManager(new LinearLayoutManager(this));
        tracks.setAdapter(adapter);


        Intent intent = getIntent();
        username.setText(intent.getStringExtra("username"));
        userRating.setText(String.valueOf(intent.getIntExtra("rating", 1)));

        getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();


    }

    @Override
    public Loader<ProfileModel> onCreateLoader(int id, Bundle args) {

        if (id == LOADER_ID) {
            return new ProfileModelLoader(this);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<ProfileModel> loader, ProfileModel data) {

        adapter.setModel(data);
        numOfTracks.setText(String.valueOf(data.getTracks().size()));

    }

    @Override
    public void onLoaderReset(Loader<ProfileModel> loader) {

    }


}
