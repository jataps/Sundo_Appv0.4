package com.example.sundoappv04;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomStudentAdapter extends RecyclerView.Adapter<CustomStudentAdapter.MyViewHolder> {

    Context context;

    ArrayList<Student> list;

    public CustomStudentAdapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_student, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Student student = list.get(position);

        holder.textFirstName.setText(student.getFirstName());
        holder.textLastName.setText(student.getLastName());
        holder.textEmail.setText(student.getCompleteAdd());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textFirstName, textLastName, textEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textFirstName = itemView.findViewById(R.id.textFirstName);
            textLastName = itemView.findViewById(R.id.textLastName);
            textEmail = itemView.findViewById(R.id.textEmail);

        }
    }

}
