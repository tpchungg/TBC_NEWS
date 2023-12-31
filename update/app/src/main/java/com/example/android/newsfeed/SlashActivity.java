package com.example.android.newsfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        },0);
    }
    private void nextActivity(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent=new Intent(this,log_in.class);
            startActivity(intent);
            finish();
        }else
        {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        finish();
    }
}