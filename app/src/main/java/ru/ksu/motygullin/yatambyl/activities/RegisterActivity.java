package ru.ksu.motygullin.yatambyl.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

import retrofit2.Response;
import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.entites.PostModel;
import ru.ksu.motygullin.yatambyl.service.YaTamBylAPI;
import ru.ksu.motygullin.yatambyl.service.YaTamBylServerService;

public class RegisterActivity extends AppCompatActivity {

    final Context context = this;
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText regUsername;
    EditText regEmail;
    EditText regPass;
    Button register;
    Button auth;
    YaTamBylAPI api;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        YaTamBylServerService serverService = new YaTamBylServerService();
        api = serverService.getApi();

        try {
            if (getIntent().getStringExtra("user").equals("logOut")) {
                FirebaseAuth.getInstance().signOut();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        mAuth = FirebaseAuth.getInstance();
        regUsername = (EditText) findViewById(R.id.reg_username);
        regEmail = (EditText) findViewById(R.id.reg_email);
        regPass = (EditText) findViewById(R.id.reg_pass);
        register = (Button) findViewById(R.id.register);
        auth = (Button) findViewById(R.id.auth);

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AuthActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!regUsername.getText().toString().equals("") && !regEmail.getText().toString().equals("")&& !regPass.getText().toString().equals("")){
                    if (regPass.getText().length()>=6) {
                        createAccount(regEmail.getText().toString(), regPass.getText().toString());
                        Toast.makeText(context, "Мы выслали сообщение на вашу почту " + regEmail.getText().toString() + ". Пройдите по ссылке, чтобы завершить регистрацию", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Пароль должен содержать более 5 символов", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void sendEmailVerification(FirebaseUser user) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(regUsername.getText().toString()).build();
        user.updateProfile(profileUpdates);

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("REG", "Email sent.");
                        }
                    }
                });
        updateUI(user);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("REG", "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            sendEmailVerification(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("REG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Ошибка регистрации. Возможно этот адрес уже зарегистрирован. Попробуйте еще раз",
                                    Toast.LENGTH_LONG).show();

                        }


                    }
                });

    }

    private void updateUI(final FirebaseUser user) {

        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()){
                    try {
                        Response<PostModel> response = api.addUsers(task.getResult().getToken(), user.getDisplayName()).execute();
                        if (response.body().getSuccess()){
                            Intent intent = new Intent(context, AuthActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d("ERROR", response.body().getError());
                            Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


}
