package com.example.sundoappv04;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sundoappv04.databinding.ActivityHomeStudentBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContainerStudent extends AppCompatActivity {

    ActivityHomeStudentBinding binding;

    MaterialButton signOutBtnStudent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new StudentFragmentHome());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navHome:
                    replaceFragment(new StudentFragmentHome());
                    break;

                case R.id.navProfile:
                    replaceFragment(new StudentFragmentProfile());
                    break;

                case R.id.navSettings:
                    replaceFragment(new StudentFragmentSettings());
                    break;

                case R.id.navRecords:
                    replaceFragment(new StudentFragmentRecords());
                    break;
            }
            return true;

        });

        mAuth = FirebaseAuth.getInstance();

        signOutBtnStudent = findViewById(R.id.signOutBtnStudent);

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


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContainerStudent.super.onBackPressed();
                    }
                }).create().show();
    }
}