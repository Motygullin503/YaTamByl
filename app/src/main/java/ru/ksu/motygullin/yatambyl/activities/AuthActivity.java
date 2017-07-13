package ru.ksu.motygullin.yatambyl.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import ru.ksu.motygullin.yatambyl.R;
import ru.ksu.motygullin.yatambyl.entites.User;

public class AuthActivity extends AppCompatActivity {

    final Context context = this;
    TextView forgetPass;
    TextView registerRef;
    EditText authEmail;
    EditText authPassword;
    Button btnAuth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        forgetPass = (TextView) findViewById(R.id.forget_pass);
        registerRef = (TextView) findViewById(R.id.register_ref);
        authEmail = (EditText) findViewById(R.id.auth_email);
        authPassword = (EditText) findViewById(R.id.auth_password);
        btnAuth = (Button) findViewById(R.id.btn_auth);

        mAuth = FirebaseAuth.getInstance();

        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!authEmail.getText().toString().equals("") && !authPassword.getText().toString().equals("")) {
                    signIn(authEmail.getText().toString(), authPassword.getText().toString());
                } else {
                    Toast.makeText(context, "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show();
                }


            }
        });

        registerRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("user", "logOut");
                startActivity(intent);
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        sendPasswordResetEmail(userInput.getText().toString());
                                        if (mAuth.getCurrentUser()!=null){
                                            mAuth.signOut();
                                        }

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


    private void sendPasswordResetEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "Email sent.");
                        }
                    }
                });
    }


    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "signInWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (!user.isEmailVerified()) {
                                    Toast.makeText(AuthActivity.this, "Email не подтвержден", Toast.LENGTH_SHORT).show();
                                } else {
                                    user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(context, GeneralActivity.class);
                                                User newUser = new User(task.getResult().getToken(), user.getDisplayName(), 1, null);
                                                intent.putExtra("user", newUser);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.w("AUTH", "signInWithEmail:failure", task.getException());
                                                Toast.makeText(AuthActivity.this, "Не удалось войти, повторите попытку.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Не удалось войти, повторите попытку.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}
