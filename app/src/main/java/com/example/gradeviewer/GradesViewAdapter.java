package com.example.gradeviewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GradesViewAdapter extends RecyclerView.Adapter<GradesViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<GradesModel> list;

    public GradesViewAdapter(Context context, ArrayList<GradesModel> list) {
        this.context = context;
        this.list = list;
    }
    
    

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grades,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GradesModel gradesModel = list.get(position);

        holder.subjectName.setText(gradesModel.getSubjectName());
        holder.subjectCode.setText(gradesModel.getSubjectCode());
        holder.prelim.setText(gradesModel.getPrelim());
        holder.midterm.setText(gradesModel.getMidterm());
        holder.finals.setText(gradesModel.getFinals());



    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView prelim,subjectName,midterm,finals,subjectCode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            prelim = itemView.findViewById(R.id.prelim_display);
            subjectName = itemView.findViewById(R.id.subject_display);
            subjectCode = itemView.findViewById(R.id.subject_code_display);
            midterm = itemView.findViewById(R.id.midterm_display);
            finals = itemView.findViewById(R.id.final_display);



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
