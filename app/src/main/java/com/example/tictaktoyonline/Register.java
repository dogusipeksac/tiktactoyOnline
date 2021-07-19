package com.example.tictaktoyonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText emailEdit;
    EditText passwordEdit;
    EditText userNameEdit;
    Button registerButton;
    TextView logintext;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailEdit=findViewById(R.id.emailEditText);
        passwordEdit=findViewById(R.id.passwordEditText);
        userNameEdit=findViewById(R.id.usernameEditText);
        registerButton=findViewById(R.id.registerButton);
        logintext=findViewById(R.id.loginTextView);

        mAuth=FirebaseAuth.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,LoginActivity.class));

            }
        });

    }
    public void createUser(){

        String email=emailEdit.getText().toString();
        String username=userNameEdit.getText().toString();
        String password=passwordEdit.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEdit.setError("email connot be empty");
            emailEdit.requestFocus();
        }
        if(TextUtils.isEmpty(username)){
            userNameEdit.setError("user name connot be empty");
            userNameEdit.requestFocus();
        }
        if(TextUtils.isEmpty(password)){
            passwordEdit.setError("password cannot be empty");
            passwordEdit.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this, "user register successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(Register.this, "Reqistration error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}