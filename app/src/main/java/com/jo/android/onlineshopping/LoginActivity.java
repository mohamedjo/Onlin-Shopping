package com.jo.android.onlineshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.onlineshopping.model.Users;
import com.jo.android.onlineshopping.prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText editTextPhoneNumber,editTextPassword;
    Button loginButton;
    ProgressDialog loadingBar;
    CheckBox checkBoxRemmberMe ;
    TextView adminPannel,notAdmonpannel;

      String parentDbName="users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextPassword=findViewById(R.id.edit_text_password);
        editTextPhoneNumber=findViewById(R.id.edit_text_phone_number);
        loginButton=findViewById(R.id.login_btn);
        checkBoxRemmberMe=findViewById(R.id.check_box_remmember_me);
        adminPannel=findViewById(R.id.tv_admin);
        notAdmonpannel=findViewById(R.id.tv_not_admin);


        Paper.init(this);
        loadingBar=new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();

            }
        });

        adminPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Logib Admin");
                adminPannel.setVisibility(View.INVISIBLE);
                notAdmonpannel.setVisibility(View.VISIBLE);
                parentDbName="admins";
            }
        });
        notAdmonpannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminPannel.setVisibility(View.VISIBLE);
                notAdmonpannel.setVisibility(View.INVISIBLE);
                parentDbName="users";
            }
        });

    }

    private void loginUser() {
        String phoneNumber =editTextPhoneNumber.getText().toString();
        String password =editTextPassword.getText().toString();

        if(TextUtils.isEmpty(phoneNumber)){

            Toast.makeText(this, "please inter a phone number...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "please inter a password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("login account");
            loadingBar.setMessage("please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            allowAccessToAcount(phoneNumber,password);
        }

    }

    private void allowAccessToAcount(final String phoneNumber, final String password) {

        if(checkBoxRemmberMe.isChecked()){

            Paper.book().write(Prevalent.userPhoneKey,phoneNumber);
            Paper.book().write(Prevalent.userPasswordKey,password);



        }
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phoneNumber).exists()){

                    Users user=dataSnapshot.child(parentDbName).child(phoneNumber).getValue(Users.class);

                    if(user.getPhone().equals(phoneNumber)){

                        if(user.getPassword().equals(password)){

                            if(parentDbName.equals("users")) {
                                Toast.makeText(LoginActivity.this, "logged in correctly!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                              //  Prevalent.currentOnlineUser=user;
                                startActivity(intent);
                            }
                            else if(parentDbName.equals("admins")){
                                Toast.makeText(LoginActivity.this, "welcome admin you are logged in correctly!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, AdminCtegoryActivity.class);
                                startActivity(intent);

                            }


                        }
                        else{

                            Toast.makeText(LoginActivity.this, "you intered incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                else{
                    Toast.makeText(LoginActivity.this, "this phone number does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
