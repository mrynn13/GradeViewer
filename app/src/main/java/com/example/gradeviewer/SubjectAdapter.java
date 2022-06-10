package com.example.gradeviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubjectModel> list;



    Dialog dialog,dialog1;
    AlertDialog.Builder dialogBuilder;

    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference subjectReference;

    String userId;

    public SubjectAdapter(Context context, ArrayList<SubjectModel> list) {
        this.context = context;
        this.list = list;
    }
    
    

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubjectModel subjectModel = list.get(position);



        fAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        userId = fAuth.getCurrentUser().getUid();


        holder.subjectCode.setText(subjectModel.getSubjectCode());
        holder.subjectName.setText(subjectModel.getSubjectName());
        holder.subjectSched.setText(subjectModel.getSubjectSched());


        holder.removeBtn.setOnClickListener(v -> {
            subjectReference=rootNode.getReference("users/"+userId+"/Subjects");
            subjectReference.child(subjectModel.getSubjectId()).removeValue();
            Intent intent = new Intent(context.getApplicationContext(),Subjects.class);
            context.startActivity(intent);
            Toast.makeText(context, "Subject Removed!", Toast.LENGTH_SHORT).show();
        });

        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 GlobalVar.codeToPass = subjectModel.getSubjectCode();
                 GlobalVar.subjectToPass = subjectModel.getSubjectName();
                 GlobalVar.idToPass = subjectModel.getSubjectId();

                Intent intent = new Intent(context,  Students.class);
                 context.startActivity(intent);


            }
        });




    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView subjectCode,subjectName,subjectSched;
        CardView viewBtn;
        Button removeBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectCode = itemView.findViewById(R.id.subject_code_display);
            subjectName = itemView.findViewById(R.id.subject_name_display);
            subjectSched = itemView.findViewById(R.id.schedule_display);
            removeBtn = itemView.findViewById(R.id.remove_class);
            viewBtn = itemView.findViewById(R.id.view);

        }
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
}
