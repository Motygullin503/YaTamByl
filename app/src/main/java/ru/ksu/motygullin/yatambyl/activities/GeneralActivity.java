package ru.ksu.motygullin.yatambyl.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.nio.charset.Charset;

import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.asynchronous.ArtworkLoader;
import ru.ksu.motygullin.yatambyl.entites.Artwork;

public class GeneralActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Artwork>, SwipeRefreshLayout.OnRefreshListener {

    public static final int LOADER_ID = 111;
    final Context context = this;
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

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Мой профиль");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Топ/Голосование");
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(2).withName("Топ/Голосование");
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(2).withName("Топ/Голосование");

        new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem(),
                        item3, new SecondaryDrawerItem(),
                        item4, new SecondaryDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            
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
