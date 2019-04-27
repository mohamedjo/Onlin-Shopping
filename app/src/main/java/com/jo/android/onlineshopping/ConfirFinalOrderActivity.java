package com.jo.android.onlineshopping;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.onlineshopping.prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirFinalOrderActivity extends AppCompatActivity {

    Button buttonConfirm;
    EditText editTextName,editTextPhone,editTextAddress,editTextCity;

    String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confir_final_order);
        totalPrice=getIntent().getStringExtra("total price");

        Toast.makeText(this, totalPrice, Toast.LENGTH_SHORT).show();
        buttonConfirm=findViewById(R.id.btn_confirm);
        editTextName=findViewById(R.id.shipper_det_name);
        editTextPhone=findViewById(R.id.shipper_det_phone);
        editTextAddress=findViewById(R.id.shipper_det_address);
        editTextCity=findViewById(R.id.shipper_det_city);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

    }

    private void check() {
           if(TextUtils.isEmpty(editTextName.getText().toString()))
        {
            Toast.makeText(this, "please inter the full name", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(editTextPhone.getText().toString()))
        {
            Toast.makeText(this, "please inter the phone", Toast.LENGTH_SHORT).show();

        }
           else if(TextUtils.isEmpty(editTextAddress.getText().toString()))
           {
               Toast.makeText(this, "please inter the address", Toast.LENGTH_SHORT).show();

           }
           else if(TextUtils.isEmpty(editTextCity.getText().toString()))
           {
               Toast.makeText(this, "please inter the city", Toast.LENGTH_SHORT).show();

           }
        else {

            confirmOrder();

        }
    }

    private void confirmOrder() {
        String saveTime,saveDate;
        Calendar calendarForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveDate=currentDate.format(calendarForDate.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveTime=currentTime.format(calendarForDate.getTime());
        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String,Object> orderMap=new HashMap<>();
        orderMap.put("tottalAmount",totalPrice);
        orderMap.put("date",saveDate);
        orderMap.put("time",saveTime);
        orderMap.put("name",editTextName.getText().toString());
        orderMap.put("phone",editTextPhone.getText().toString());
        orderMap.put("address",editTextAddress.getText().toString());
        orderMap.put("city",editTextCity.getText().toString());

        orderMap.put("state","not shipped");
        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("cart list")
                            .child("user view")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(ConfirFinalOrderActivity.this, "your final order confirmed successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirFinalOrderActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }
        });
    }
}
