package com.example.sundoappv04;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.sundoappv04.databinding.ActivityContainerDriverBinding;
import com.example.sundoappv04.databinding.ActivityContainerStudentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContainerDriver extends AppCompatActivity {

    FirebaseAuth mAuth;

    ActivityContainerDriverBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContainerDriverBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        replaceFragment(new DriverFragmentHome());

        binding.bottomNavigationViewDriver.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navHomeDriver:
                    replaceFragment(new DriverFragmentHome());
                    break;

                case R.id.navProfileDriver:
                    replaceFragment(new DriverFragmentProfile());
                    break;

                case R.id.navServiceDriver:
                    replaceFragment(new DriverFragmentService());
                    break;

                case R.id.navRecordsDriver:
                    replaceFragment(new DriverFragmentRecords());
                    break;
            }
            return true;

        });

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_driver, fragment);
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
                        ContainerDriver.super.onBackPressed();
                    }
                }).create().show();
    }
}