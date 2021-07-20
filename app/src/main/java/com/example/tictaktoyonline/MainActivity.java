package com.example.tictaktoyonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    EditText inviteEditTextMail;
    EditText myUserEmailEdit;
    String email;
    String uid;
    Button accept;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inviteEditTextMail=findViewById(R.id.inviteEditText);
        myUserEmailEdit=findViewById(R.id.editTextMyEmail);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);
        mAuth=FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
                else{
                    uid=currentUser.getUid();
                    email=currentUser.getEmail();
                    myUserEmailEdit.setText(email);
                    myRef.child("Users").child(beforeAt(email))
                            .child("Request").setValue(uid);
                    inComingRequest();

                }
            }
        };

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    String beforeAt(String email){
        String[] split=email.split("@");
        return split[0];
    }
    String playerSession="";
    String mySample="X";
    void startGame(String playerGameId){
        playerSession=playerGameId;
        myRef.child("Playing").child(playerGameId).removeValue();

        // Read from the database
        myRef.child("Playing").child(playerGameId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            player1.clear();
                            player2.clear();
                            activePlayer=2;
                            HashMap<String,Object> td=
                                    (HashMap<String,Object>)dataSnapshot.getValue();
                            if(td!=null){
                                String value;

                                for(String key:td.keySet()){
                                    value=(String)td.get(key);
                                    if(!value.equals(beforeAt(email)))
                                        activePlayer=mySample=="X"?1:2;
                                    else
                                        activePlayer=mySample=="X"?2:1;

                                   String[] split =key.split(":");
                                   AutoPlay(Integer.parseInt(split[1]));
                                }

                            }

                        }catch (Exception exception){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

    }

    public void clickButton(View view) {
        //game not started
        if (playerSession.length()<=0){
            return;
        }

        Button buttonSelected=(Button) view;
        int sellId=0;
        switch (buttonSelected.getId()){
            case R.id.button1:
                sellId=1;
                break;
            case R.id.button2:
                sellId=2;
                break;
            case R.id.button3:
                sellId=3;
                break;
            case R.id.button4:
                sellId=4;
                break;
            case R.id.button5:
                sellId=5;
                break;
            case R.id.button6:
                sellId=6;
                break;
            case R.id.button7:
                sellId=7;
                break;
            case R.id.button8:
                sellId=8;
                break;
            case R.id.button9:
                sellId=9;
                break;
        }
       myRef.child("Playing").child(playerSession).child("CellId:"+sellId).setValue(beforeAt(email));
    }
    int activePlayer=1;
    ArrayList<Integer> player1=new ArrayList<Integer>(); // player 1 data
    ArrayList<Integer> player2=new ArrayList<Integer>(); // player 2 data
    void PlayGame(int sellID,Button buttonSelected){

        if (activePlayer==1){
            // Toast.makeText(this,"Played player 1",Toast.LENGTH_SHORT).show();
            buttonSelected.setText("X");

            player1.add(sellID);
            buttonSelected.setBackgroundColor(Color.GREEN);
            //  Toast.makeText(this,"Turn player 2",Toast.LENGTH_SHORT).show();



        }
        else if(activePlayer==2){
            // Toast.makeText(this,"Played player 2",Toast.LENGTH_SHORT).show();
            buttonSelected.setText("O");

            player2.add(sellID);
            buttonSelected.setBackgroundColor(Color.RED);
            // Toast.makeText(this,"Turn player 1",Toast.LENGTH_SHORT).show();
        }
        buttonSelected.setEnabled(false);

        CheckWinner();
    }


    int CheckWinner(){
        int winner=-1;
        //row 1
        if(player1.contains(1)&&player1.contains(2)&&player1.contains(3)){
            winner=1;
        }
        if(player2.contains(1)&&player2.contains(2)&&player2.contains(3)){
            winner=2;
        }
        //row 2
        if(player1.contains(4)&&player1.contains(5)&&player1.contains(6)){
            winner=1;
        }
        if(player2.contains(4)&&player2.contains(5)&&player2.contains(6)){
            winner=2;
        }
        //row 3
        if(player1.contains(7)&&player1.contains(8)&&player1.contains(9)){
            winner=1;
        }
        if(player2.contains(7)&&player2.contains(8)&&player2.contains(9)){
            winner=2;
        }
        //colomn 1
        if(player1.contains(1)&&player1.contains(4)&&player1.contains(7)){
            winner=1;
        }
        if(player2.contains(1)&&player2.contains(4)&&player2.contains(7)){
            winner=2;
        }
        //colomn 2
        if(player1.contains(2)&&player1.contains(5)&&player1.contains(8)){
            winner=1;
        }
        if(player2.contains(2)&&player2.contains(5)&&player2.contains(8)){
            winner=2;
        }
        //colomn 3
        if(player1.contains(3)&&player1.contains(6)&&player1.contains(9)){
            winner=1;
        }
        if(player2.contains(3)&&player2.contains(6)&&player2.contains(9)){
            winner=2;
        }
        if(winner!=-1){
            if(winner==1){
                Toast.makeText(this,"Player 1 is winner",Toast.LENGTH_LONG).show();

            }
            if(winner==2){
                Toast.makeText(this,"Player 2 is winner",Toast.LENGTH_LONG).show();
            }
        }
        return winner;

    }
    void AutoPlay(int cellID){

        Button buttonSelected;

        switch (cellID){
            case 1:
                buttonSelected=(Button) findViewById(R.id.button1);
                break;
            case 2:
                buttonSelected=(Button) findViewById(R.id.button2);
                break;
            case 3:
                buttonSelected=(Button) findViewById(R.id.button3);
                break;
            case 4:
                buttonSelected=(Button) findViewById(R.id.button4);
                break;
            case 5:
                buttonSelected=(Button) findViewById(R.id.button5);
                break;
            case 6:
                buttonSelected=(Button) findViewById(R.id.button6);
                break;
            case 7:
                buttonSelected=(Button) findViewById(R.id.button7);
                break;
            case 8:
                buttonSelected=(Button) findViewById(R.id.button8);
                break;
            case 9:
                buttonSelected=(Button) findViewById(R.id.button9);
                break;
            default:
                buttonSelected=(Button) findViewById(R.id.button1);
                break;
        }
        PlayGame(cellID,buttonSelected);
    }

    public void invateButton(View view) {
        myRef.child("Users").child(beforeAt(inviteEditTextMail
                .getText().toString()))
                .child("Request").push().setValue(email);
        startGame(beforeAt(inviteEditTextMail.getText().toString())
                +":"+beforeAt(email));
        mySample="X";
    }
    void inComingRequest(){
// Read from the database
        myRef.child("Users")
                .child(beforeAt(email))
                .child("Request")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            HashMap<String,Object> td=
                                    (HashMap<String,Object>)dataSnapshot.getValue();
                            if(td!=null){
                                String value;
                                for(String key:td.keySet()){
                                    value=(String)td.get(key);
                                    inviteEditTextMail.setText(value);
                                    buttonColor();
                                    myRef.child("Users")
                                            .child(beforeAt(email))
                                            .child("Request").setValue(uid);

                                    break;
                                }

                            }

                        }catch (Exception exception){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void acceptButton(View view) {
        myRef.child("Users").child(beforeAt(inviteEditTextMail
                .getText().toString()))
                .child("Request").push().setValue(email);
        startGame(beforeAt(email)
                +":"+beforeAt(inviteEditTextMail.getText().toString()));
        mySample="O";

    }



    public void logoutButton(View view) {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
    void buttonColor(){
        inviteEditTextMail.setBackgroundColor(Color.RED);
    }
}