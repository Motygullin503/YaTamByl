package ru.ksu.motygullin.yatambyl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.ksu.motygullin.yatambyl.R;

public class VotingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        finish();
    }
}
