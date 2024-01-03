package com.example.android.newsfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView img_avatar;
    Uri mUri;
    FirebaseFirestore db;  // Declare the variable here

    ProgressDialog progressDialog;
EditText edt_Name, edt_Phone,edt_PassWord;
TextView Text_email;

Button btnUpdateProfile,btnUpdatePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        edt_Name=findViewById(R.id.edt_full_name);
        Text_email=findViewById(R.id.edt_email);
        edt_Phone=findViewById(R.id.edt_phone);
        edt_PassWord=findViewById(R.id.edt_resetPassword);
        img_avatar=(ImageView) findViewById(R.id.img_avatar);
        btnUpdateProfile=findViewById(R.id.btn_update);
        btnUpdatePassword=findViewById(R.id.btn_Password);
        progressDialog=new ProgressDialog(this);
        db= FirebaseFirestore.getInstance();


        //Bắt sự kiện set thông tin
        setUserInformation();
        //ChangePassWord
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassWord();
            }
        });
        //Bắt sự kiện update
        initListener();
    }
    //Change Password
    private void updatePassWord() {
        String strnewPassword = edt_PassWord.getText().toString().trim();
        progressDialog.show();
if(strnewPassword.isEmpty()){
    progressDialog.dismiss();
    Toast.makeText(MyProfile.this, "Nhập mật khẩu mới",
            Toast.LENGTH_SHORT).show();
    return;
}else{
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(strnewPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(MyProfile.this, "Cập nhật mật khẩu mới thành công",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyProfile.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else{
                            progressDialog.dismiss();
                            Intent intent = new Intent(MyProfile.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(MyProfile.this, "Cập nhật thành công",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }}


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
        mUri = user.getPhotoUrl();
        Text_email.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(img_avatar);

    }
    private void initListener() {
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();


            }

        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });


    }

//Update profile
    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
String strName,strPhone;
strName=edt_Name.getText().toString().trim();
strPhone=edt_Phone.getText().toString().trim();
        progressDialog.show();

if(mUri==null){UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        .setDisplayName(strName)
        .build();

    user.updateProfile(profileUpdates)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("phoneNumber", strPhone);

                        db.collection("users")
                                .document(user.getUid())
                                .set(userMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(MyProfile.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MyProfile.this, "ch add phone",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }
            });
}else {
    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(strName)
            .setPhotoUri(Uri.parse(mUri.toString().trim()))
            .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("phoneNumber", strPhone);

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(MyProfile.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MyProfile.this, "ch add phone",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                });

    }
    }

    private void openGallery() {
        // Sử dụng Intent để chọn ảnh từ thư viện
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            if(imageUri!=null){
                mUri=imageUri;
            }
            // Hiển thị ảnh đã chọn lên ImageView
            Glide.with(this).load(imageUri).error(R.drawable.ic_avatar_default).into(img_avatar);
        }
    }

}