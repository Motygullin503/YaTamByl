package ru.ksu.motygullin.yatambyl.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.entites.PostModel;
import ru.ksu.motygullin.yatambyl.service.Api;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;

public class RecordingActivity extends AppCompatActivity {

    final static int PERMISSION_REQUEST_CODE = 666;
    public final Activity context = this;
    final FirebaseStorage storage = FirebaseStorage.getInstance();

    FloatingActionButton record;
    FloatingActionButton stop;
    TextView text;
    TextView pressButton;
    String filePath;


    private boolean intialStage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        final String uid = UUID.randomUUID().toString()+".m4a";
        record = (FloatingActionButton) findViewById(R.id.record);
        stop = (FloatingActionButton) findViewById(R.id.stop);
        text = (TextView) findViewById(R.id.poem_text_rec);
        pressButton = (TextView) findViewById(R.id.br);

        Intent intent = getIntent();
        text.setText(intent.getStringExtra("text"));

        requestPermission();
        final MediaRecorder recorder = new MediaRecorder();
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filePath = Environment.getExternalStorageDirectory() + "/" + "YaTamBylZapis" + ".m4a";
                record.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
                recorder.setAudioChannels(1);
                recorder.setAudioSamplingRate(44100);
                recorder.setAudioEncodingBitRate(96000);
                pressButton.setText("Остановить запись");
                recorder.setOutputFile(filePath);
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recorder.start();
            }

        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.stop();
                recorder.reset();

                record.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                pressButton.setText("Нажмите, чтобы начать запись");

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.recording_prompt, null);

                //Создаем AlertDialog
                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final FloatingActionButton btnPlay = (FloatingActionButton) promptsView.findViewById(R.id.play);
                final ProgressBar progressPlay = (ProgressBar) promptsView.findViewById(R.id.progress_play);

                final MediaPlayer mediaPlayer = new MediaPlayer();
                btnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnPlay.setClickable(false);
                        try {
                            mediaPlayer.setDataSource(filePath);
                            Log.d("FILE", filePath);
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                                progressPlay.setMax(mediaPlayer.getDuration());
                            }
                        });

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                btnPlay.setClickable(true);
                                mediaPlayer.reset();
                            }
                        });



                    }
                });

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        StorageReference reference = storage.getReference(uid);
                                        Uri file = Uri.fromFile(new File(filePath));
                                        final UploadTask task = reference.putFile(file);
                                        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Log.d("UPLOAD", "Success");
                                                Intent intent = getIntent();
                                                YaTamBylAPI api = Api.getInstance().getApi();

//                                                Call<PostModel> call= api.addTracks(intent.getStringExtra("uid"), intent.getIntExtra("artwork_id", 1), String.valueOf(task.getResult().getDownloadUrl()));
//                                                call.enqueue(new Callback<PostModel>() {
//                                                    @Override
//                                                    public void onResponse(@NonNull Call<PostModel> call, @NonNull Response<PostModel> response) {
//                                                        Toast.makeText(RecordingActivity.this, "Запись успешно загружена!", Toast.LENGTH_LONG).show();
//                                                        Intent intent = new Intent(context, GeneralActivity.class);
//                                                        startActivity(intent);
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(@NonNull Call<PostModel> call, @NonNull Throwable t) {
//                                                        Toast.makeText(RecordingActivity.this, "Произошла ошибка. Повторите попытку.", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("UPLOAD", "Failure");
                                            }
                                        });

                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();
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

}
