package com.example.sundoappv04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.emergency.EmergencyNumber;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.HashMap;

public class FillUpForm extends AppCompatActivity {

    TextView uidText, provinceText, cityText, barangayText;
    TextInputEditText editTextFirstName, editTextLastName, editTextMiddleName, editTextPhoneNumber, editTextEmergencyName, editTextEmergencyNumber, ediTextAddNotes;
    MaterialButton signOutBtnStudent, submitBtnStudent;
    String userType, selectedProvince, selectedCity = "Select your city", selectedBarangay;
    Spinner provinceSpinner, citySpinner, barangaySpinner;
    ArrayAdapter<CharSequence> provinceAdapter, cityAdapter, barangayAdapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_up_form);


        //spinner initialization
        provinceSpinner = findViewById(R.id.provinceSpinner);

        //populate arrayadapter using string array and a spinner layout that we will define
        provinceAdapter = ArrayAdapter.createFromResource(this, R.array.array_provinces, R.layout.spinner_layout);

        //specify layout to use when the list of choices appear
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set the adapter to the spinner to populate the Provinces Spinner
        provinceSpinner.setAdapter(provinceAdapter);

        //when any item in province spinner is selected
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                citySpinner = findViewById(R.id.citySpinner);

                //get selected province
                selectedProvince = provinceSpinner.getSelectedItem().toString();

                int parentID = adapterView.getId();
                if(parentID == R.id.provinceSpinner){
                    switch(selectedProvince){
                        case "Select your province": cityAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_default_cities, R.layout.spinner_layout);
                            break;

                        case "Rizal": cityAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_rizal_cities, R.layout.spinner_layout);
                            break;

                        default: break;
                    }
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    //populate the cities according to selected province
                    citySpinner.setAdapter(cityAdapter);

                    //To obtain the selected city from the cityspinner
                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            barangaySpinner = findViewById(R.id.barangaySpinner);

                            selectedCity = citySpinner.getSelectedItem().toString();

                            int parentID = adapterView.getId();

                            if(parentID == R.id.citySpinner) {
                                switch (selectedCity) {
                                    case "Select your city":
                                        barangayAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_default_barangays, R.layout.spinner_layout);
                                        break;

                                    case "Angono":
                                        barangayAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_angono_barangays, R.layout.spinner_layout);
                                        break;

                                }

                                barangayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                //populate the cities according to selected province
                                barangaySpinner.setAdapter(barangayAdapter);

                                barangaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        selectedBarangay = barangaySpinner.getSelectedItem().toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sundo-app-44703-default-rtdb.firebaseio.com/");

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();

        //Edit Texts
        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextMiddleName = findViewById(R.id.middleName);
        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        editTextEmergencyName = findViewById(R.id.emergencyName);
        editTextEmergencyNumber = findViewById(R.id.emergencyNumber);
        ediTextAddNotes = findViewById(R.id.addressNote);

        //Material Buttons
        signOutBtnStudent = findViewById(R.id.signOutBtnStudent);
        submitBtnStudent = findViewById(R.id.submitBtnStudent);

        //TextViews
        uidText = findViewById(R.id.uidText);
        provinceText = findViewById(R.id.provinceText);
        cityText = findViewById(R.id.cityText);
        barangayText = findViewById(R.id.barangayText);

        //filters
        editTextFirstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextLastName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextMiddleName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextEmergencyName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        ediTextAddNotes.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        editTextPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        editTextEmergencyNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);


        dbRef.child("users").child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    //if current user does exist we read its content here users > student > UID <- start here
                    if (task.getResult().exists()) {

                        dbRef.child("users").child(currentUser).child("status").setValue("online");

                        DataSnapshot dataSnapshot = task.getResult();
                        userType = String.valueOf(dataSnapshot.child("userType").getValue());


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
                    Toast.makeText(FillUpForm.this, "User does not exist!", Toast.LENGTH_SHORT);
                }

            }
        });

        submitBtnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = String.valueOf(editTextFirstName.getText());
                String lastName = String.valueOf(editTextLastName.getText());
                String middleName = String.valueOf(editTextMiddleName.getText());
                String contactNumber = String.valueOf(editTextPhoneNumber.getText());

                String emergencyName = String.valueOf(editTextEmergencyName.getText());
                String emergencyNumber = String.valueOf(editTextEmergencyNumber.getText());

                String province = String.valueOf(selectedProvince);
                String city = String.valueOf(selectedCity);
                String barangay = String.valueOf(selectedBarangay);

                String addNote = String.valueOf(ediTextAddNotes.getText());

                if (TextUtils.isEmpty(firstName)) {
                    editTextFirstName.setError("Enter first name!");
                    editTextFirstName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    editTextLastName.setError("Enter last name!");
                    editTextLastName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(middleName)) {
                    editTextMiddleName.setError("Enter middle name!");
                    editTextMiddleName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(contactNumber)) {
                    editTextMiddleName.setError("Enter valid number!");
                    editTextMiddleName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(emergencyName)) {
                    editTextEmergencyName.setError("Enter valid name !");
                    editTextEmergencyName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(emergencyNumber)) {
                    editTextEmergencyNumber.setError("Enter valid number!");
                    editTextEmergencyNumber.requestFocus();
                    return;
                }

                if (selectedProvince.equals("Select your province")) {
                    provinceText.setError("Select province!");
                    provinceText.requestFocus();
                    return;
                }

                if (selectedCity.equals("Select your city")) {
                    ((TextView)citySpinner.getSelectedView()).setError("Select city");
                    citySpinner.requestFocus();
                    return;
                }

                if (selectedBarangay.equals("Select your barangay")) {
                    barangayText.setError("Select barangay");
                    barangayText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(addNote)) {
                    ediTextAddNotes.setError("Enter note!");
                    ediTextAddNotes.requestFocus();
                    return;
                }

                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String completeAdd = addNote + " Brgy. " + barangay + ", " + city + ", " + province;

                DatabaseReference userRef = dbRef.child("users").child(currentUser).child("recordID");
                DatabaseReference recordRef = dbRef.child("records");

                String requestID = recordRef.push().getKey();
                userRef.setValue(requestID);

                HashMap map = new HashMap();
                map.put("uid", currentUser);
                map.put("lastName", lastName);
                map.put("firstName", firstName);
                map.put("middleName", middleName);
                map.put("contactNumber", contactNumber);
                map.put("userType", userType);

                map.put("emergencyName", emergencyName);
                map.put("emergencyNumber", emergencyNumber);

                map.put("address/longitude", false);
                map.put("address/latitude", false);
                map.put("address/completeAdd", completeAdd);

                recordRef.child(requestID).updateChildren(map);

                Toast.makeText(FillUpForm.this, "Register Successful", Toast.LENGTH_SHORT);
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

    public void populateSpinner() {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        FillUpForm.super.onBackPressed();
                    }
                }).create().show();
    }
}