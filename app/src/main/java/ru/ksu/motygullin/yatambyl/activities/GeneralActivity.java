package ru.ksu.motygullin.yatambyl.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.mikepenz.materialdrawer.DrawerBuilder;

import ru.ksu.motygullin.yatambyl.R;

public class GeneralActivity extends AppCompatActivity {

    Button beginRecording;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        beginRecording = (Button) findViewById(R.id.begin_recording);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .build();

        beginRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordingActivity.class);
                startActivity(intent);
            }
        });
    }
}
