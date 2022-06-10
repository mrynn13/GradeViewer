package com.example.gradeviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ViewGrades extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<GradesModel> list;

    GradesViewAdapter gradeAdapter;
    TextView name,tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grades);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303f9f")));

        setTitleColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo1);
        getSupportActionBar().setTitle("Grades Viewer");

        String id = getIntent().getStringExtra("IDNUM");
        name = findViewById(R.id.student_name_display);
        tv = findViewById(R.id.tv);
        tv.setVisibility(View.GONE);

        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.grade_list);

        reference = rootNode.getReference("Grades").child(id);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        gradeAdapter = new GradesViewAdapter(this,list);

        gradeAdapter.setHasStableIds(true);
        recyclerView.setAdapter(gradeAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Toast.makeText(ViewGrades.this, "No Records Found", Toast.LENGTH_SHORT).show();
                }

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    GradesModel gradesModel = dataSnapshot.getValue(GradesModel.class);
                    list.add(gradesModel);
                }
                goToStudent(id);

                gradeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void goToStudent(String id) {
        DatabaseReference sRef = FirebaseDatabase.getInstance().getReference("Students");
        DatabaseReference sIdRef = sRef.child(id);
        DatabaseReference nameRef = sIdRef.child("studentName");

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    return;
                }
                String sName = dataSnapshot.getValue().toString();
                if (!TextUtils.isEmpty(sName)){
                    tv.setVisibility(View.VISIBLE);
                    name.setText(sName);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewGrades.this,User.class);
        startActivity(intent);
        finishAffinity();
    }
}