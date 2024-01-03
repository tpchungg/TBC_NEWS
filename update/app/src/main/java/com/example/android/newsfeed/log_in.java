package com.example.android.newsfeed;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class log_in extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnSignIn;
    TextView textForgotPassword;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        mAuth=FirebaseAuth.getInstance();
        edtEmail=findViewById(R.id.editText_email);
        edtPassword=findViewById(R.id.editText_password);
        btnSignIn=findViewById(R.id.button_sign_in);
        progressDialog=new ProgressDialog(this);
        textForgotPassword=findViewById(R.id.textView_forgotPassword);
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassWord();

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail= String.valueOf(edtEmail.getText());
                String strPassword=String.valueOf(edtPassword.getText());
                if(TextUtils.isEmpty(strEmail)){
                    Toast.makeText(log_in.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strPassword)){
                    Toast.makeText(log_in.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(log_in.this, "Login Susscess",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(log_in.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    // Sign in success, update UI with the signed-in user's information

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(log_in.this, "Email hoặc mật khẩu không đúng",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    private void onClickForgotPassWord() {
       progressDialog.show();
        String emailAddress =edtEmail.getText().toString().trim();

        if(emailAddress.isEmpty()){
            Toast.makeText(this, "Nhập email", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            }
        else{
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(log_in.this, "Email sent", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(log_in.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });}
    }


    public void register(View view) {
        Intent intent = new Intent(this,register.class);
        startActivity(intent);
        finish();
    }
}