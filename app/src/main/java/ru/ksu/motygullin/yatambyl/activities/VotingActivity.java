package ru.ksu.motygullin.yatambyl.activities;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.adapters.ProfileRecyclerViewAdapter;
import ru.ksu.motygullin.yatambyl.adapters.TopRecyclerViewAdapter;
import ru.ksu.motygullin.yatambyl.asynchronous.TopTracksLoader;
import ru.ksu.motygullin.yatambyl.entites.TopModel;

public class VotingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<TopModel>, SwipeRefreshLayout.OnRefreshListener{

    public static final int LOADER_ID = 333;

    TopRecyclerViewAdapter adapter;
    TextView uploads;
    RecyclerView tracks;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        uploads = (TextView) findViewById(R.id.num_of_tracks);
        tracks = (RecyclerView) findViewById(R.id.tracks);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_rf);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new TopRecyclerViewAdapter(this, null);
        tracks.setLayoutManager(new LinearLayoutManager(this));
        tracks.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

    }

    @Override
    public Loader<TopModel> onCreateLoader(int id, Bundle args) {

        if (id == LOADER_ID){
            swipeRefreshLayout.setRefreshing(true);
            return new TopTracksLoader(this);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<TopModel> loader, TopModel data) {
        if (data!=null) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setModel(data);
            uploads.setText(String.valueOf(data.getTracks().size()));
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onLoaderReset(Loader<TopModel> loader) {

    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
