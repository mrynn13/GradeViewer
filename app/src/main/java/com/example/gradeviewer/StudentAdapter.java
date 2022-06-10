package com.example.gradeviewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<StudentModel> list;
    ArrayList<StudentModel> clistFull;
    String prelimG;
    String midtermG;
    String finalsG;



    Dialog dialog,dialog1;
    AlertDialog.Builder dialogBuilder;

    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference studentReference,gradeRef;


    String userId;

    public StudentAdapter(Context context, ArrayList<StudentModel> list) {
        this.context = context;
        this.clistFull= list;
        this.list = new ArrayList<>(clistFull);
    }
    
    

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentModel studentModel = list.get(position);



        fAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        userId = fAuth.getCurrentUser().getUid();


        holder.studentName.setText(studentModel.getName());
        holder.studentId.setText(studentModel.getStudentid());


        holder.removeBtn.setOnClickListener(v -> {
            studentReference=rootNode.getReference("users/"+userId+"/Students");
            studentReference.child(studentModel.getUid()).removeValue();
            Intent intent = new Intent(context.getApplicationContext(),Students.class);
            context.startActivity(intent);
            Toast.makeText(context, "Student Removed!", Toast.LENGTH_SHORT).show();
        });

        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //get the sId of current item
                String studentID = studentModel.getUid();
                String sClass =studentModel.getStudentSubject();

                dialog = new Dialog(context);

                dialog.setContentView(R.layout.add_grade);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                EditText prelim,midterm,finals;
                Button save,cancel;

                save = dialog.findViewById(R.id.save_btn);
                cancel = dialog.findViewById(R.id.cancel_btn);
                prelim = dialog.findViewById(R.id.prelim_et);
                midterm = dialog.findViewById(R.id.midterm_et);
                finals = dialog.findViewById(R.id.finals_et);

                prelim.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                midterm.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                finals.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);

                gradeRef = rootNode.getReference("Grades/"+studentModel.getStudentid());

                gradeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()){
                            Toast.makeText(context, "No Grades Yet", Toast.LENGTH_SHORT).show();

                        }else{
                            gradeRef = rootNode.getReference("Grades/"+studentModel.getStudentid()).child(studentModel.getStudenStCode());
                            gradeRef.get().addOnCompleteListener(task -> {

                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        DataSnapshot mySnapshot = task.getResult();

                                        prelim.setText(String.valueOf(mySnapshot.child("prelim").getValue()));
                                        midterm.setText(String.valueOf(mySnapshot.child("midterm").getValue()));
                                        finals.setText(String.valueOf(mySnapshot.child("finals").getValue()));
                                    }
                                }

                            });


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                save.setOnClickListener(v2->{
                    prelimG = prelim.getText().toString();
                    midtermG = midterm.getText().toString();
                    finalsG = finals.getText().toString();
                    
                    if (!TextUtils.isEmpty(prelimG) && (Integer.parseInt(prelimG)<60 || Integer.parseInt(prelimG) > 100)){
                        Toast.makeText(context, "Invalid Grade", Toast.LENGTH_SHORT).show();
                        prelim.setError("Invalid");
                        return;
                    }
                    if (!TextUtils.isEmpty(finalsG) && (Integer.parseInt(midtermG)<60 || Integer.parseInt(midtermG) > 100)){
                        Toast.makeText(context, "Invalid Grade", Toast.LENGTH_SHORT).show();
                        prelim.setError("Invalid");
                        return;
                    }
                    if (!TextUtils.isEmpty(finalsG) && (Integer.parseInt(finalsG)<60 || Integer.parseInt(finalsG) > 100)){
                        Toast.makeText(context, "Invalid Grade", Toast.LENGTH_SHORT).show();
                        prelim.setError("Invalid");
                        return;
                    }

                    gradeRef = rootNode.getReference("Grades/"+studentModel.getStudentid());
                    GradesModel gradesModel = new GradesModel(studentModel.getName(),studentModel.getStudenStCode(),studentModel.getStudentSubject(),prelimG,midtermG,finalsG);
                    gradeRef.child(studentModel.getStudenStCode()).setValue(gradesModel);


                    Toast.makeText(context.getApplicationContext(), "Grades Saved", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });

               cancel.setOnClickListener(v1->{
                   dialog.dismiss();


               });

                dialog.show();







            }
        });




    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<StudentModel> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(clistFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (StudentModel studentModel : clistFull){
                    if (studentModel.getName().toLowerCase().contains(filterPattern) || studentModel.getStudentid().contains(filterPattern)){
                        filteredList.add(studentModel);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            filterResults.count = filteredList.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();

        }
    };


    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView studentName,studentId;
        LinearLayout viewBtn;
        Button removeBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name_display);
            studentId = itemView.findViewById(R.id.student_id_display);
            removeBtn = itemView.findViewById(R.id.remove_student);
            viewBtn = itemView.findViewById(R.id.cv);

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
