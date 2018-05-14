package com.healthcare.manojkulkarni.firebasefinal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.healthcare.Adapter.ListAdapterNear;
import com.healthcare.ModelClass.UserClass;
import com.healthcare.R;

import java.util.ArrayList;

import okhttp3.internal.Internal;

public class FinalNearBy extends AppCompatActivity {

    ListView NearByList;

    DatabaseReference databaseReference;


    Button Search;
    EditText EditName;

    ArrayList Name , Contact , Address , LastDonate , BloodGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_near_by);

        EditName = (EditText) findViewById(R.id.EditSearch);
        Search = (Button) findViewById(R.id.ButtnSearch);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Name = new ArrayList();
        Contact = new ArrayList();
        Address = new ArrayList();
        LastDonate = new ArrayList();
        BloodGroup = new ArrayList();

        NearByList = (ListView) findViewById(R.id.NearByList);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EditName.getText().toString().isEmpty()){


                    Name.clear();
                    Contact.clear();
                    Address.clear();
                    LastDonate.clear();
                    BloodGroup.clear();
                    NearByList.setAdapter(null);
                    GETUSER();

                }else {

                    Name.clear();
                    Contact.clear();
                    Address.clear();
                    LastDonate.clear();
                    BloodGroup.clear();
                    NearByList.setAdapter(null);
                    GETUSERSEARCH();

                }
            }
        });


        GETUSER();


    }


    public  void GETUSER(){

        databaseReference.child("DATA").child("USERS_DATA").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){


                    UserClass userClass = dataSnapshot1.getValue(UserClass.class);

                    Name.add(userClass.getFirstName());
                    Contact.add(userClass.getContact());
                    Address.add(userClass.getAddress());
                    LastDonate.add(userClass.getDonate());
                    BloodGroup.add(userClass.getBloodgroup());



                }

                BindData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public  void GETUSERSEARCH() {

        String name = EditName.getText().toString().trim();

        databaseReference.child("DATA").child("USERS_DATA").orderByChild("firstName").equalTo(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){



                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){



                        UserClass userClass = dataSnapshot1.getValue(UserClass.class);

                        Name.add(userClass.getFirstName());
                        Contact.add(userClass.getContact());
                        Address.add(userClass.getAddress());
                        LastDonate.add(userClass.getDonate());
                        BloodGroup.add(userClass.getBloodgroup());




                    }

                    BindData();

                }else {

                    Toast.makeText(FinalNearBy.this, "No Found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }




    public void  BindData(){


        ListAdapterNear adapterNear = new ListAdapterNear(FinalNearBy.this , Name , Contact , Address , LastDonate , BloodGroup);
        NearByList.setAdapter(adapterNear);


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(FinalNearBy.this , FinalMain.class);
        startActivity(intent);
        this.finish();
    }
}
