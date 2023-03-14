package com.example.sundoappv04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeStudent extends AppCompatActivity {

    TextView studentName, studentEmail;
    MaterialButton signOutStudentBtn;
    FirebaseAuth mAuth;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sundo-app-44703-default-rtdb.firebaseio.com/");

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onStart() {
        super.onStart();

        dbRef.child("users").child("student").child(currentUser).child("status").setValue("ONLINE");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        mAuth = FirebaseAuth.getInstance();

        studentName = findViewById(R.id.nameStudent);
        studentEmail = findViewById(R.id.emailStudent);
        signOutStudentBtn = findViewById(R.id.signOutStudentBtn);

        dbRef.child("users").child("student").child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    //if current user does exist we read its content here users > student > UID <- start here
                    if (task.getResult().exists()) {

                        DataSnapshot dataSnapshot = task.getResult();
                        String email = String.valueOf(dataSnapshot.child("email").getValue());
                        String password = String.valueOf(dataSnapshot.child("password").getValue());

                        studentEmail.setText(email);
                        studentName.setText(password);

                    }

                } else {
                    Toast.makeText(HomeStudent.this, "User does not exist!", Toast.LENGTH_SHORT);
                }

            }
        });

        signOutStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}