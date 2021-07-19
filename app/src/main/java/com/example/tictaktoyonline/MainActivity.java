package com.example.tictaktoyonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText userEmailEdit;
    EditText myUserEmailEdit;
    String email;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userEmailEdit=findViewById(R.id.editTextUserEmail);
        myUserEmailEdit=findViewById(R.id.editTextMyEmail);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);
        mAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
            }
        };

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        else{
            email=currentUser.getEmail();
            myUserEmailEdit.setText(email);
            myRef.child("Users").child(beforeAt(email))
                    .child("Request").setValue(currentUser.getUid());
        }
    }
    String beforeAt(String email){
        String[] split=email.split("@");
        return split[0];
    }

    public void clickButton(View view) {
    }

    public void invateButton(View view) {
        Log.d("Invate",userEmailEdit.getText().toString());
    }

    public void acceptButton(View view) {
        Log.d("Accept",userEmailEdit.getText().toString());
    }


    public void logoutButton(View view) {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}