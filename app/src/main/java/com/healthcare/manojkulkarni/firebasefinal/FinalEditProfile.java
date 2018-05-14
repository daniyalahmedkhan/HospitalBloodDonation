package com.healthcare.manojkulkarni.firebasefinal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthcare.ModelClass.UserClass;
import com.healthcare.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FinalEditProfile extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    EditText Name, Contact , addres , donate , bloodgroup;
    String N, C , A , D , B , E , LN , P , UID , TodayDate;
    Button SaveProfile;
    Calendar mCalender;
    int month , day , year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_edit_profile);

        /// Getting Current Date and Time //
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        TodayDate = dateFormat.format(cal.getTime());
        ///


        mCalender = Calendar.getInstance();
        day = mCalender.get(Calendar.DAY_OF_MONTH);
        month = mCalender.get(Calendar.MONTH);
        year = mCalender.get(Calendar.YEAR);

        databaseReference = FirebaseDatabase.getInstance().getReference("DATA");
        firebaseAuth = FirebaseAuth.getInstance();

        Name = (EditText) findViewById(R.id.Name);
        Contact = (EditText) findViewById(R.id.Contact);
        addres = (EditText) findViewById(R.id.addres);
        donate = (EditText) findViewById(R.id.donate);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
        SaveProfile = (Button) findViewById(R.id.SaveProfile);


        SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SAVEDATA();

            }
        });


        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FinalEditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        i1 = i1+1;

                        //Toast.makeText(BookingActivity.this , " " +i2 + "/" + i1 + "/" + i , Toast.LENGTH_SHORT ).show();
                        donate.setText(i2 + "/" + i1 + "/" + i);

                    }
                } , year , month , day);
                datePickerDialog.show();
            }


        });

        GETDATA();


    }


    public  void GETDATA(){


        databaseReference.child("USERS_DATA").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){


                    try {


                        UserClass userClass = dataSnapshot.getValue(UserClass.class);



                        Name.setText(userClass.getFirstName());


                        Contact.setText(userClass.getContact());


                        addres.setText(userClass.getAddress());

                        donate.setText(userClass.getDonate());


                        bloodgroup.setText(userClass.getBloodgroup());

                        E = userClass.getEmail();
                        P = userClass.getPassword();
                        UID = userClass.getUserID();
                        LN = userClass.getLastName();


                    }catch (Exception e){


                    }


                }else{


                    Toast.makeText(FinalEditProfile.this, "Not Found Data", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void SAVEDATA(){


        if (!(Name.getText().toString().isEmpty() || Contact.getText().toString().isEmpty() ||

                addres.getText().toString().isEmpty() || donate.getText().toString().isEmpty() ||

                bloodgroup.getText().toString().isEmpty())){


            N = Name.getText().toString().trim();
            C = Contact.getText().toString().trim();
            A = addres.getText().toString().trim();
            D = donate.getText().toString().trim();
            B = bloodgroup.getText().toString().trim();



            UserClass userClass = new UserClass(E , P , UID , N , LN , C , A , B , D);

            databaseReference.child("USERS_DATA").child(firebaseAuth.getCurrentUser().getUid()).setValue(userClass).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Intent intent = new Intent(FinalEditProfile.this , FinalProfile.class);
                    startActivity(intent);
                    FinalEditProfile.this.finish();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });



        }else {


            Toast.makeText(this, "Please Enter Fields ..", Toast.LENGTH_SHORT).show();

        }

    }
}
