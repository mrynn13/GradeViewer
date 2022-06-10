package com.example.gradeviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
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

public class Students extends AppCompatActivity {
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
   StudentAdapter mainAdapter;
    ArrayList<StudentModel> list;
    private EditText student_name,student_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303f9f")));

        setTitleColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Students");

        drawerLayout = findViewById(R.id.drawer_layout);
        dialog = new Dialog(this);
        add = findViewById(R.id.add_student);
        fAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recycle_view);
        teacherName = findViewById(R.id.nameTeacher);

        userId = fAuth.getCurrentUser().getUid();

        reference = rootNode.getReference("users/"+userId+"/"+GlobalVar.idToPass).child("Students");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();




        recyclerView.setAdapter(mainAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);
                    list.add(studentModel);
                }

                mainAdapter = new StudentAdapter(Students.this,list);
                recyclerView.setAdapter(mainAdapter);

                mainAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        add.setOnClickListener(v -> {
            dialog.setContentView(R.layout.add_student);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            student_name = dialog.findViewById(R.id.student_name_et);
            student_id = dialog.findViewById(R.id.student_id_et);
            saveBtn = dialog.findViewById(R.id.save_btn);
            cancelBtn = dialog.findViewById(R.id.cancel_btn);

            dialog.show();

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String studentCode = GlobalVar.codeToPass;
                    String studentName = student_name.getText().toString();
                    String studentId = student_id.getText().toString();
                    String UID = UUID.randomUUID().toString();
                    String studentSubject = GlobalVar.subjectToPass;

                    if (TextUtils.isEmpty(studentName) || TextUtils.isEmpty(studentId) ){
                        student_name.setError("Required");
                        student_id.setError("Required");
                        return;
                    }

                    if (!TextUtils.isEmpty(studentName) && !TextUtils.isEmpty(studentId )){
                        //insert data
                        list.clear();
                        StudentModel studentModel = new StudentModel(studentName,studentId,studentSubject,studentCode,UID);
                        reference.child(UID).setValue(studentModel);
                        recreate();
                        Toast.makeText( Students.this, "Student Added", Toast.LENGTH_SHORT).show();

                        GoToStudent(studentName,studentId,studentCode);

                    }else{
                        student_name.setError("Required");
                        student_id.setError("Required");
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

    private void GoToStudent(String studentName, String studentId, String studentCode) {
        StudentListModel studentModel = new StudentListModel(studentName,studentId,studentCode);
        reference = rootNode.getReference("Students/"+studentId);
        reference.setValue(studentModel);
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

    public  void ClickArea(View view){
        Subjects.closeDrawer(drawerLayout);
    }
    public  void ClickHome(View view){
        Subjects.redirectActivity(this,Subjects.class);
    }
    public  void ClickAbout(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("This App is created for grade viewing");
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Students.this, "Thanks", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }
    public void ClickLogout(View view){
        //finish activity
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Students.this, Login.class);
        startActivity(intent);
        finishAffinity();
    }

    public void search(View view) {
        openOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Students...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mainAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Students.this,Subjects.class);
        startActivity(intent);
        GlobalVar.codeToPass="";
        GlobalVar.idToPass="";
        GlobalVar.subjectToPass="";
        finishAffinity();
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
}