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

public class HomeDriver extends AppCompatActivity {

    TextView driverName, driverEmail, uidText;
    MaterialButton signOutBtnDriver, serviceBtnDriver;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_driver);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sundo-app-44703-default-rtdb.firebaseio.com/");

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();

        driverName = findViewById(R.id.nameDriver);
        driverEmail = findViewById(R.id.emailDriver);

        serviceBtnDriver = findViewById(R.id.serviceBtnDriver);
        signOutBtnDriver = findViewById(R.id.signOutBtnDriver);

        uidText = findViewById(R.id.uidText);

        dbRef.child("USERS").child("DRIVER").child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        dbRef.child("users").child(currentUser).child("status").setValue("online");

                        DataSnapshot dataSnapshot = task.getResult();

                        String uid = String.valueOf(dataSnapshot.child("uid").getValue()); //test textview

                        uidText.setText(uid); //test textview

                    } else {
                        Toast.makeText(HomeDriver.this, "Account does not exist!", Toast.LENGTH_SHORT);
                    }

                } else {
                    Toast.makeText(HomeDriver.this, "Account does not exist!", Toast.LENGTH_SHORT);
                }

            }
        });

        serviceBtnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceDriver.class);
                startActivity(intent);
            }
        });

        signOutBtnDriver.setOnClickListener(new View.OnClickListener() {
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
                        HomeDriver.super.onBackPressed();
                    }
                }).create().show();
    }
}