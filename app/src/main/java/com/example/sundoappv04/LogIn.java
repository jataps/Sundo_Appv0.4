package com.example.sundoappv04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    MaterialButton registerBtn;
    MaterialButton loginBtn;
    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;
    TextView forgotPass;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase fireDb = FirebaseDatabase.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //if last user has logged in and verified, auto-sign in will trigger
        if((currentUser != null) && (currentUser.isEmailVerified())) {

            String uid = mAuth.getCurrentUser().getUid();
            filterType2(uid);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        registerBtn = findViewById(R.id.registerbtn);
        loginBtn = findViewById(R.id.loginbtn);
        editTextEmail = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        forgotPass = findViewById(R.id.forgotPass);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterMain.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText()).trim();
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Enter Email!");
                    editTextEmail.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter password!");
                    editTextPassword.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                // Check if email is verified
                                if(!(mAuth.getCurrentUser().isEmailVerified())){
                                    Toast.makeText(getApplicationContext(), "Please verify your email.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String uid = mAuth.getCurrentUser().getUid();

                                filterType2(uid);

                            } else {
                                // If log in fails, display a message to the user.
                                Toast.makeText(LogIn.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

    }

    void filterType2 (String uid) {
        // Login successful, get the user's UID
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // Check if the user is a student or a driver
        ref.child("USERS").child("DRIVER").orderByChild("UID").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // User with the given UID exists in DRIVER
                    Intent driverIntent = new Intent(getApplicationContext(), FillUpForm.class);
                    startActivity(driverIntent);
                    finish();
                } else {
                    // User with the given UID does not exist in DRIVER
                    // Check if it exists in STUDENT
                    ref.child("USERS").child("STUDENT").orderByChild("UID").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // User is a STUDENT
                                Intent studentIntent = new Intent(getApplicationContext(), FillUpForm.class);
                                startActivity(studentIntent);
                                finish();

                            } else {
                                // User with the given UID does not exist in either DRIVER or STUDENT node

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }

    void filterType (String uid) {

        fireDb.getReference().child("users").child(uid).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userType = String.valueOf(snapshot.getValue());

                switch (userType) {
                    case "driver":
                        Intent driverIntent = new Intent(getApplicationContext(), FillUpForm.class);
                        startActivity(driverIntent);
                        finish();
                        break;

                    case "student":
                        Intent studentIntent = new Intent(getApplicationContext(), FillUpForm.class);
                        startActivity(studentIntent);
                        finish();
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        LogIn.super.onBackPressed();
                    }
                }).create().show();
    }
}