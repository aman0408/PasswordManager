package com.spitcomps.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText emailEditText, passwordEditText;
    Button loginButton, sinUpButton;
    String email, password;
    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    AlertDialog nameAlert;
    Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCtx = this;

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton = findViewById(R.id.loginButton);
        sinUpButton = findViewById(R.id.signUpButton);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();
            }
        });
        sinUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });
    }

    private void logInUser() {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Please Enter Email");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter Valid Email");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Please Enter Password");
            passwordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password needs at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mCtx, PasswordsOverviewActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signUpUser() {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Please Enter Email");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter Valid Email");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Please Enter Password");
            passwordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password needs at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }
        final AlertDialog.Builder nameAlertBuilder = new AlertDialog.Builder(LoginActivity.this);
        View nameAlertView = getLayoutInflater().inflate(R.layout.name_alert, null);
        final EditText nameET = nameAlertView.findViewById(R.id.nameET);
        nameAlertBuilder.setCancelable(false)
                .setTitle("Name")
                .setMessage("Please Enter Your Name")
                .setView(nameAlertView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String name = nameET.getText().toString().trim();
                        Log.i(TAG, "onClick: " + name);
                        if (name.equals("")) {
                            nameET.setError("Please Enter Name");
                            Toast.makeText(mCtx,"Please Enter Name",Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(mCtx,"Please Wait",Toast.LENGTH_SHORT).show();
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(name, email);
                                        usersRef.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(LoginActivity.this, "Welcome, " + name, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, PasswordsOverviewActivity.class);
                                                finish();
                                                startActivity(intent);
                                            }
                                        });
                                    }else {
                                        Toast.makeText(mCtx,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
        nameAlert = nameAlertBuilder.create();

        nameAlert.show();
    }
}
