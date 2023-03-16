package com.example.sundoappv04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeStudent extends AppCompatActivity {

    TextView studentName, studentEmail, uidText;
    TextInputEditText ETfirstName, ETlastName, ETmiddleName, ETprovince, ETcity, ETbrgy, ETaddNote;
    MaterialButton signOutBtnStudent, submitBtnStudent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sundo-app-44703-default-rtdb.firebaseio.com/");

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();

        ETfirstName = findViewById(R.id.studFirstName);
        ETlastName = findViewById(R.id.studLastName);
        ETmiddleName= findViewById(R.id.studMiddleName);
        ETprovince = findViewById(R.id.studProvince);
        ETcity = findViewById(R.id.studCity);
        ETbrgy = findViewById(R.id.studBrgy);
        ETaddNote = findViewById(R.id.studAddNote);

        studentName = findViewById(R.id.nameStudent);
        studentEmail = findViewById(R.id.emailStudent);

        signOutBtnStudent = findViewById(R.id.signOutBtnStudent);
        submitBtnStudent = findViewById(R.id.submitBtnStudent);

        uidText = findViewById(R.id.uidText);

        dbRef.child("users").child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    //if current user does exist we read its content here users > student > UID <- start here
                    if (task.getResult().exists()) {

                        dbRef.child("users").child(currentUser).child("status").setValue("online");

                        DataSnapshot dataSnapshot = task.getResult();
                        /*
                        String email = String.valueOf(dataSnapshot.child("email").getValue());

                        String userType = String.valueOf(dataSnapshot.child("userType").getValue());
                        String uid = String.valueOf(dataSnapshot.child("uid").getValue()); //test textview

                        studentEmail.setText(email);
                        studentName.setText(userType);
                        uidText.setText(uid); //test textview
                         */

                    }

                } else {
                    Toast.makeText(HomeStudent.this, "User does not exist!", Toast.LENGTH_SHORT);
                }

            }
        });


        submitBtnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = String.valueOf(ETfirstName.getText());
                String lastName = String.valueOf(ETlastName.getText());
                String middleName = String.valueOf(ETmiddleName.getText());
                String province = String.valueOf(ETprovince.getText());
                String city = String.valueOf(ETcity.getText());
                String brgy = String.valueOf(ETbrgy.getText());
                String addNote = String.valueOf(ETaddNote.getText());

                if (TextUtils.isEmpty(firstName)) {
                    ETfirstName.setError("Enter first name!");
                    ETfirstName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    ETlastName.setError("Enter last name!");
                    ETlastName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(middleName)) {
                    ETmiddleName.setError("Enter middle name!");
                    ETmiddleName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(province)) {
                    ETprovince.setError("Enter province!");
                    ETprovince.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(city)) {
                    ETcity.setError("Enter city!");
                    ETcity.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(brgy)) {
                    ETbrgy.setError("Enter barangay!");
                    ETbrgy.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(addNote)) {
                    ETaddNote.setError("Enter note!");
                    ETaddNote.requestFocus();
                    return;
                }

                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                dbRef.child("records").child("students").child(currentUser).child("lastName").setValue(lastName);
                dbRef.child("records").child("students").child(currentUser).child("firstName").setValue(firstName);
                dbRef.child("records").child("students").child(currentUser).child("middleName").setValue(middleName);
                dbRef.child("records").child("students").child(currentUser).child("province").setValue(province);
                dbRef.child("records").child("students").child(currentUser).child("city").setValue(city);
                dbRef.child("records").child("students").child(currentUser).child("barangay").setValue(brgy);
                dbRef.child("records").child("students").child(currentUser).child("addressNote").setValue(addNote);

                Toast.makeText(HomeStudent.this, "Register Successful", Toast.LENGTH_SHORT);

            }
        });

        signOutBtnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.child("users").child(currentUser).child("status").setValue("offline");

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void checkEmpty(){



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