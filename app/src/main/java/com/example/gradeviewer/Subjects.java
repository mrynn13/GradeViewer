package com.example.gradeviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Subjects extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference,teacherRef;
    String userId;

    DrawerLayout drawerLayout;
    Dialog dialog;
    TextView teacherName;
    EditText subject_code,subject_name,subject_sched;
    Button cancelBtn, saveBtn,okBtn,copyCode;
    ImageButton add;

    RecyclerView recyclerView;
    SubjectAdapter mainAdapter;
    ArrayList<SubjectModel> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303f9f")));

        setTitleColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subjects");

        drawerLayout = findViewById(R.id.drawer_layout);
        dialog = new Dialog(this);
        add = findViewById(R.id.add_class);
        fAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recycle_view);
        teacherName = findViewById(R.id.nameTeacher);

        userId = fAuth.getCurrentUser().getUid();

        reference = rootNode.getReference("users/"+userId).child("Subjects");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        mainAdapter = new SubjectAdapter(this,list);

        mainAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mainAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    SubjectModel subjectModel = dataSnapshot.getValue(SubjectModel.class);
                    list.add(subjectModel);
                }
                gotoTeacher();

                mainAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        add.setOnClickListener(v -> {
            dialog.setContentView(R.layout.add_sub);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            subject_code = dialog.findViewById(R.id.subject_code_et);
            subject_name = dialog.findViewById(R.id.subject_name_et);
            subject_sched = dialog.findViewById(R.id.subject_sched_et);
            saveBtn = dialog.findViewById(R.id.save_btn);
            cancelBtn = dialog.findViewById(R.id.cancel_btn);

            dialog.show();

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subjectCode = subject_code.getText().toString().trim();
                    String subjectName = subject_name.getText().toString();
                    String subjectSched = subject_sched.getText().toString();
                    String subjectID = UUID.randomUUID().toString();

                    if (TextUtils.isEmpty(subjectCode) || TextUtils.isEmpty(subjectName) || TextUtils.isEmpty(subjectSched)){
                        subject_code.setError("Required");
                        return;
                    }

                    if (!TextUtils.isEmpty(subjectCode) && !TextUtils.isEmpty(subjectName ) && !TextUtils.isEmpty(subjectSched)){
                        //insert data
                        list.clear();
                        SubjectModel subjectModel = new SubjectModel(subjectName,subjectCode.toUpperCase(),subjectSched,subjectID);
                       reference.child(subjectID).setValue(subjectModel);
                       recreate();
                       finish();


                        Toast.makeText( Subjects.this, "Subject Added", Toast.LENGTH_SHORT).show();

                    }else{
                        subject_code.setError("Required");
                        subject_name.setError("Required");
                        subject_sched.setError("Required");
                    }
                    dialog.dismiss();

                }
            });

            cancelBtn.setOnClickListener((View view)->{
                //cancel
                dialog.dismiss();
            });

        });


    }

    private void gotoTeacher() {
        teacherRef = rootNode.getReference("Teacher");
        teacherRef.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot mySnapshot = task.getResult();

                    teacherName.setText(String.valueOf(mySnapshot.child("name").getValue()));
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    Subjects.closeDrawer(drawerLayout);
                else
                    Subjects.openDrawer(drawerLayout);


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void ClickAbout(View view){
        popUpMessage();

    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        // open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickArea(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        //Homeactivity
        recreate();
    }
    public void ClickLogout(View view){
        //logout
        logout(this);
    }
    public void logout(Activity activity) {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set title
        builder.setTitle("Logout");
        //set message
        builder.setMessage("Are you sure you want to Logout?");
        //positive yes
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish activity
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(Subjects.this, Login.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.show();


    }
    public static void redirectActivity(Activity activity,Class directClass){
        //initialize intent
        Intent intent = new Intent(activity,directClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Exit");
        //set message
        builder.setMessage("Are you sure you want to Exit?");
        //positive yes
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish activity
                finishAffinity();
                System.exit(0);
            }
        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.show();
    }

    public void popUpMessage(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Subjects.this);
        alert.setMessage("This App is created for grade viewing");
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Subjects.this, "Thanks", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }
}