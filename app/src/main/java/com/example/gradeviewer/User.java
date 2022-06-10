package com.example.gradeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class User extends AppCompatActivity {
    Button teach;
    LinearLayout cont;
    EditText id;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            startActivity(new Intent(this,Subjects.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        teach = findViewById(R.id.teacher);
        cont = findViewById(R.id.container);
        id = findViewById(R.id.idnum);
    }

    public void Teach(View view) {
        Intent intent = new Intent(User.this,Login.class);
        startActivity(intent);
        finishAffinity();
    }

    public void Student(View view) {
        teach.setVisibility(View.GONE);
        cont.setVisibility(View.VISIBLE);


    }

    public void ViewGrades(View view) {
        String idnumber= id.getText().toString();

        if (TextUtils.isEmpty(idnumber)){
            id.setError("Required");
            return;
        }else{
            Intent intent = new Intent(User.this,ViewGrades.class);
            intent.putExtra("IDNUM",idnumber);
            startActivity(intent);
        }
    }
}