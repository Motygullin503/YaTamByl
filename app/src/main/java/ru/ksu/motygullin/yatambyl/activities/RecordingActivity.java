package ru.ksu.motygullin.yatambyl.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import ru.ksu.motygullin.yatambyl.R;

public class RecordingActivity extends AppCompatActivity {

    final Activity context = this;
    FloatingActionButton record;
    FloatingActionButton stopRecord;
    final static int PERMISSION_REQUEST_CODE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);


        record =(FloatingActionButton) findViewById(R.id.record);
        stopRecord =(FloatingActionButton) findViewById(R.id.stop_record);

        requestPermission();

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.m4a";
                int requestCode = 0;

                    AndroidAudioRecorder.with(context)
                            // Required
                            .setFilePath(filePath)
                            .setColor(R.color.colorPrimaryDark)
                            .setRequestCode(requestCode)

                            // Optional
                            .setSource(AudioSource.MIC)
                            .setChannel(AudioChannel.MONO)
                            .setSampleRate(AudioSampleRate.HZ_11025)
                            .setAutoStart(false)
                            .setKeepDisplayOn(true)

                            // Start recording
                            .record();
                }

        });

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }
    }
}
