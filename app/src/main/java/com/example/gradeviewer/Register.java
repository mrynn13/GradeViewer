package com.example.gradeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    ProgressBar pBar;

    EditText fullName,emailEt,password,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Teacher");
    }

    public void OnSignUpClicked(View view) {
        pBar = findViewById(R.id.progress_bar);
        pBar.setVisibility(View.VISIBLE);
        fullName = findViewById(R.id.name_sign_up);
        emailEt = findViewById(R.id.email_sign_up);
        password = findViewById(R.id.password_sign_up);
        confirmPassword = findViewById(R.id.confirm_password_sign_up);

        fAuth = FirebaseAuth.getInstance();

        String name = fullName.getText().toString();
        String email = emailEt.getText().toString();
        String pass = password.getText().toString();
        String conPass = confirmPassword.getText().toString();



        if (TextUtils.isEmpty(name)){
            fullName.setError("Name is Required");
            pBar.setVisibility(View.GONE);
            return;
        }else if (TextUtils.isEmpty(email)){
            emailEt.setError("Email is Required");
            pBar.setVisibility(View.GONE);
            return;
        }else if (TextUtils.isEmpty(pass)){
            password.setError("password is Required");
            pBar.setVisibility(View.GONE);
        }else if (TextUtils.isEmpty(conPass)){
            confirmPassword.setError("Field Required");
            pBar.setVisibility(View.GONE);
            return;
        }else if (!pass.equals(conPass)){
            confirmPassword.setError("Mismatched Characters");
            password.setError("Mismatched Characters");
            pBar.setVisibility(View.GONE);
            return;
        } else if (pass.length() < 6){
            password.setError("Too short Password!");
            pBar.setVisibility(View.GONE);
            return;
        }else{



            fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {

                if (task.isSuccessful()){
                    String uid = fAuth.getCurrentUser().getUid();
                    UserModel userModel = new UserModel(name,uid);

                    reference.setValue(userModel);



                    Toast.makeText( Register.this,"Registration is Successful",Toast.LENGTH_SHORT).show();

                    pBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }else{
                    Toast.makeText( Register.this,"Some error Encountered"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    pBar.setVisibility(View.GONE);

                }

            });

        }

    }
    public void SignInText(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finishAffinity();
    }
}