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

public class LoginActivity extends AppCompatActivity {


    EditText loginEditMail;
    EditText loginEditPasw;
    Button login;
    TextView register;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register=findViewById(R.id.registerTextView);
        loginEditPasw=findViewById(R.id.passwordEditTextLogin);
        loginEditMail=findViewById(R.id.emailEditTextLogin);
        login=findViewById(R.id.loginButton);

        mAuth=FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Register.class));
            }
        });
    }

    public void loginUser(){
        String loginUserName=loginEditMail.getText().toString();
        String passwordLogin=loginEditPasw.getText().toString();


        if(TextUtils.isEmpty(loginUserName)){
            loginEditMail.setError("email connot be empty");
            loginEditMail.requestFocus();
        }
        if(TextUtils.isEmpty(passwordLogin)){
            loginEditPasw.setError("password connot be empty");
            loginEditPasw.requestFocus();
        }
        else{
            mAuth.signInWithEmailAndPassword(loginUserName,passwordLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete( Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "login error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}