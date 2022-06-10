package com.example.gradeviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email, password;
    ProgressBar pBar;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        pBar = findViewById(R.id.progress_bar);
        fAuth = FirebaseAuth.getInstance();
    }

    public void OnLogInClick(View view) {
        pBar = findViewById(R.id.progress_bar);
        pBar.setVisibility(View.VISIBLE);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        fAuth = FirebaseAuth.getInstance();

        String emailLogin = email.getText().toString();
        String passLogin = password.getText().toString();

        if (TextUtils.isEmpty(emailLogin)) {
            email.setError("Email is Required");
            pBar.setVisibility(View.GONE);
            return;
        } else if (TextUtils.isEmpty(passLogin)) {
            password.setError("Password is Required");
            pBar.setVisibility(View.GONE);
            return;
        }else {

            fAuth.signInWithEmailAndPassword(emailLogin,passLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText( Login.this, "Logged In", Toast.LENGTH_SHORT).show();
                        pBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),Subjects.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText( Login.this, "Log In Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        pBar.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    public void SignUptext(View view) {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
    }
}