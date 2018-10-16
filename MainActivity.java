package com.spitcomps.passwordmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null){
            Intent intent=new Intent(this,LoginActivity.class);
            finish();
            startActivity(intent);
        }else{
            Intent intent=new Intent(this,PasswordsOverviewActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
