package com.example.sundoappv04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverAddStudent extends AppCompatActivity {

    RecyclerView studentList;
    DatabaseReference mRef;
    CustomStudentAdapter adapter;
    ArrayList<Student> list;

    MaterialButton addBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_add_student);

        studentList = findViewById(R.id.studentList);
        addBtnConfirm = findViewById(R.id.addBtnConfirm);


        mRef = FirebaseDatabase.getInstance().getReference().child("USER_INFORMATION").child("STUDENT");

        studentList.setHasFixedSize(true);
        studentList.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new CustomStudentAdapter(this, list);
        studentList.setAdapter(adapter);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Student student = dataSnapshot.getValue(Student.class);
                    list.add(student);

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ContainerDriver.class);
                intent.putExtra("fragment_to_display","fragment_service");
                startActivity(intent);
                finish();
            }
        });

    }
}