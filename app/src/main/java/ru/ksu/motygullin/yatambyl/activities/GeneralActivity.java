package ru.ksu.motygullin.yatambyl.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.nio.charset.Charset;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.asynchronous.ArtworkLoader;
import ru.ksu.motygullin.yatambyl.entites.Artwork;
import ru.ksu.motygullin.yatambyl.entites.User;
import ru.ksu.motygullin.yatambyl.service.Api;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;

public class GeneralActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Artwork>, SwipeRefreshLayout.OnRefreshListener {

    public static final int LOADER_ID = 111;
    final Activity context = this;
    final long ONE_MEGABYTE = 1024 * 1024;
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final Charset charset = Charset.forName("UTF-8");
    Button beginRecording;
    ProgressBar progressBar;
    TextView poemText;
    TextView nameAndAuthor;
    String uid;
    Integer artworkId;
    CharSequence sequence;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        beginRecording = (Button) findViewById(R.id.begin_recording);
        poemText = (TextView) findViewById(R.id.poem_text);
        nameAndAuthor = (TextView) findViewById(R.id.name_and_author);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_rf);
        swipeRefreshLayout.setOnRefreshListener(this);

        ProfileDrawerItem item1 = new ProfileDrawerItem().withIdentifier(1).withName("Мой профиль");
        ProfileDrawerItem item2 = new ProfileDrawerItem().withIdentifier(2).withName("Топ/Голосование");
        ProfileDrawerItem item3 = new ProfileDrawerItem().withIdentifier(3).withName("Поиск по произведениям");


        new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        item2,
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 0:


                                YaTamBylAPI api = Api.getInstance().getApi();
                                Call<User> call = api.getUserByHash(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                        if (response.isSuccessful()){
                                            if (response.body()!=null){
                                                User user = response.body();
                                                Intent intent = new Intent(context, MyProfileActivity.class);
                                                intent.putExtra("username", user.getName());
                                                intent.putExtra("rating", user.getRating());
                                                startActivity(intent);
                                            }

                                        } else {
                                            Log.d("WARN", "OSHIBOCHKA");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                        Log.e("EROOR", "KAPEZ");
                                    }
                                });




                            case 3:
                                Intent intent1 = new Intent(context, VotingActivity.class);
                                Log.d("WARN", "Pressed");
                                startActivity(intent1);
                            case 2:
                                Intent intent2 = new Intent(context, VotingActivity.class);
                                Log.d("WARN", "Pressed");
                                startActivity(intent2);
                        }
                        return true;
                    }
                })
                .build();

        beginRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordingActivity.class);
                intent.putExtra("text", sequence);
                intent.putExtra("artwork_id", artworkId);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @Override
    public Loader<Artwork> onCreateLoader(int id, Bundle args) {

        progressBar.setVisibility(View.VISIBLE);
        if (id == LOADER_ID) {
            swipeRefreshLayout.setRefreshing(true);
            return new ArtworkLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, final Artwork data) {

        if (data != null) {
            StorageReference storageRef = storage.getReference(data.getLink());
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    sequence = new String(bytes, charset);
                    poemText.setText(sequence);
                    artworkId = data.getId();
                }
            });
            nameAndAuthor.setText("'" + data.getName() + "'\n" + data.getAuthor());
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            nameAndAuthor.setText("На сегодня заданий нет!");
            poemText.setText("Вы большой молодец!");
            beginRecording.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
