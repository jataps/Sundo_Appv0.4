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

    TextView studentName, studentEmail, uidText;
    MaterialButton signOutBtnStudent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sundo-app-44703-default-rtdb.firebaseio.com/");

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();

        studentName = findViewById(R.id.nameStudent);
        studentEmail = findViewById(R.id.emailStudent);
        signOutBtnStudent = findViewById(R.id.signOutBtnStudent);
        uidText =findViewById(R.id.uidText);

        dbRef.child("users").child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    //if current user does exist we read its content here users > student > UID <- start here
                    if (task.getResult().exists()) {

                        dbRef.child("users").child(currentUser).child("status").setValue("online");

                        DataSnapshot dataSnapshot = task.getResult();
                        String email = String.valueOf(dataSnapshot.child("email").getValue());
                        String userType = String.valueOf(dataSnapshot.child("userType").getValue());
                        String uid = String.valueOf(dataSnapshot.child("uid").getValue()); //test textview

                        studentEmail.setText(email);
                        studentName.setText(userType);
                        uidText.setText(uid); //test textview

                    }

                } else {
                    Toast.makeText(HomeStudent.this, "User does not exist!", Toast.LENGTH_SHORT);
                }

            }
        });

        signOutBtnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.child("users").child(currentUser).child("status").setValue("offline");

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}