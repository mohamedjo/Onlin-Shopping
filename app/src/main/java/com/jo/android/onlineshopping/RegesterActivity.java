package com.jo.android.onlineshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegesterActivity extends AppCompatActivity {

    Button createAcountButton;
    EditText editTextName,editTextPassword,editTextPhoneNumber;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);
        createAcountButton=findViewById(R.id.register_btn);
        editTextName=findViewById(R.id.register_edit_text_name);
        editTextPassword=findViewById(R.id.register_edit_text_password);
        editTextPhoneNumber=findViewById(R.id.regester_edit_text_phone_number);
        loadingBar= new ProgressDialog(this);

        createAcountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });


    }

    private void createNewAccount() {
        String name =editTextName.getText().toString();
        String phoneNumber =editTextPhoneNumber.getText().toString();
        String password =editTextPassword.getText().toString();

        if(TextUtils.isEmpty(name)){

            Toast.makeText(this, "please inter a name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNumber)){

            Toast.makeText(this, "please inter a phone number...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "please inter a password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("create account");
            loadingBar.setMessage("please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            validatePhoneNumber(name,phoneNumber,password);

        }



    }

    private void validatePhoneNumber(final String name, final String phoneNumber, final String password) {
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("users")).child(phoneNumber).exists()){
                    HashMap<String,Object> userDataMap=new HashMap<>();

                    userDataMap.put("name",name);
                    userDataMap.put("phone",phoneNumber);
                    userDataMap.put("password",password);
                    rootRef.child("users").child(phoneNumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegesterActivity.this, "you crreate your account successfuly", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent=new Intent(RegesterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(RegesterActivity.this, "Network Error please try agin", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                }
                else{
                    Toast.makeText(RegesterActivity.this, "this number is already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegesterActivity.this, "inter another phone number", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegesterActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
