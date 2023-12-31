package com.example.android.newsfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText edtEmail,edtPassword,edtName,edtPhone;
    Button btnSignUp;
    FirebaseAuth mAuth;
    FirebaseFirestore db;  // Declare the variable here

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        mAuth=FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        edtEmail=findViewById(R.id.editText_email_sign_up);
        edtPassword=findViewById(R.id.editText_password_sign_up);
        edtName=findViewById(R.id.editText_userName_sign_up);
        edtPhone=findViewById(R.id.editText_phone_sign_up);
        btnSignUp=findViewById(R.id.button_sign_up_sign_up);
        progressDialog=new ProgressDialog(this);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strEmail= String.valueOf(edtEmail.getText());
                String strPassword=String.valueOf(edtPassword.getText());
                String strName=String.valueOf(edtName.getText());
                String strPhone=String.valueOf(edtPhone.getText());
                String image="";
                if(TextUtils.isEmpty(strEmail)){
                    Toast.makeText(register.this,"Điền đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strPhone)){
                    Toast.makeText(register.this,"Điền đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strName)){
                    Toast.makeText(register.this,"Điền đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strPassword)){
                    Toast.makeText(register.this,"Điền đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Store additional user information in Firestore
                                    if (user != null) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(strName)
                                                .setPhotoUri(Uri.parse(image))
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Lưu số điện thoại vào Firestore
                                                            Map<String, Object> userMap = new HashMap<>();
                                                            userMap.put("phoneNumber", strPhone);

                                                            db.collection("users")
                                                                    .document(user.getUid())
                                                                    .set(userMap)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Intent intent = new Intent(register.this, MainActivity.class);
                                                                            startActivity(intent);
                                                                            finishAffinity();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(register.this, "ch add phone",
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                        } else {
                                                            Toast.makeText(register.this, "Email hoặc mật khẩu không đúng",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }});

            }
        });


    }



    public void view_login(View view) {
        Intent intent = new Intent(register.this, log_in.class);
        startActivity(intent);
        finish();
    }
}
