package com.example.android.newsfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfile extends AppCompatActivity {
ImageView img_avatar;
EditText edt_Name, edt_Email, edt_Phone;
Button btnUpdateProfile,btnBackMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        edt_Name=findViewById(R.id.edt_full_name);
        edt_Email=findViewById(R.id.edt_email);
        edt_Phone=findViewById(R.id.edt_phone);
        img_avatar=(ImageView) findViewById(R.id.img_avatar);
        btnUpdateProfile=findViewById(R.id.btn_update);
        btnBackMain=findViewById(R.id.btn_back);
        //Bắt sự kiện set thông tin
        setUserInformation();
        //Back về main activity
        btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MyProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Bắt sự kiện update
        initListener();
    }



    //Set thông tin user hiện tại
    private void setUserInformation(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());


        if(user==null){
        return;
    }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String phoneNumber = document.getString("phoneNumber");
                        edt_Phone.setText(phoneNumber);

                    } else {
                        Toast.makeText(MyProfile.this, "ch add phone",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {Toast.makeText(MyProfile.this, "ch add phone",
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
        edt_Name.setText(user.getDisplayName());
        edt_Email.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(img_avatar);

    }
    private void initListener() {
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}