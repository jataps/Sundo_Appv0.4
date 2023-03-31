package com.example.sundoappv04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeStudent extends AppCompatActivity {

    TextView studentName, studentEmail, uidTextStudent;
    MaterialButton signOutBtnStudent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sundo-app-44703-default-rtdb.firebaseio.com/");

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();

        studentName = findViewById(R.id.nameStudent);
        studentEmail = findViewById(R.id.emailStudent);
        signOutBtnStudent = findViewById(R.id.signOutBtnStudent);
        uidTextStudent = findViewById(R.id.uidTextStudent);

/*
        dbRef.child("users").child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        dbRef.child("users").child(currentUser).child("status").setValue("online");

                        DataSnapshot dataSnapshot = task.getResult();

                        String uid = String.valueOf(dataSnapshot.child("uid").getValue()); //test textview

                        uidText.setText(uid); //test textview

                    } else {
                        Toast.makeText(HomeStudent.this, "Account does not exist!", Toast.LENGTH_SHORT);
                    }

                } else {
                    Toast.makeText(HomeStudent.this, "Account does not exist!", Toast.LENGTH_SHORT);
                }

            }
        });
         */

        signOutBtnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        HomeStudent.super.onBackPressed();
                    }
                }).create().show();
    }
}