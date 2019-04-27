package com.jo.android.onlineshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.onlineshopping.model.Users;
import com.jo.android.onlineshopping.prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button loginButton, registerButton;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.main_login_btn);
        registerButton = findViewById(R.id.main_join_btn);
        loadingBar=new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegesterActivity.class);
                startActivity(intent);
            }
        });

        Paper.init(this);

        String userPhoneValue = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordValue = Paper.book().read(Prevalent.userPasswordKey);
        if (userPhoneValue != "" && userPasswordValue != "") {

            if (!TextUtils.isEmpty(userPasswordValue)&&!TextUtils.isEmpty(userPhoneValue)){

                loadingBar.setTitle("you are already logged in");
                loadingBar.setMessage("please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                allowAcsess(userPasswordValue,userPhoneValue);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userPhoneValue = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordValue = Paper.book().read(Prevalent.userPasswordKey);

    }

    private void allowAcsess(final String password, final String phoneNumber) {
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("users").child(phoneNumber).exists()){

                    Users user=dataSnapshot.child("users").child(phoneNumber).getValue(Users.class);

                    if(user.getPhone().equals(phoneNumber)){

                        if(user.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "logged in correctly!", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentOnlineUser=user;
                            startActivity(intent);
                            finish();

                        }
                        else{

                            Toast.makeText(MainActivity.this, "you intered incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                else{
                    Toast.makeText(MainActivity.this, "this phone number does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

