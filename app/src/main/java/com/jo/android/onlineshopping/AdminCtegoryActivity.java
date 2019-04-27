package com.jo.android.onlineshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCtegoryActivity extends AppCompatActivity {
   private ImageView hats,watches,bags,mobiles,laptops,tShirts,sportTshirts,shoes,headphones,femaleDresses,sweather,glasses;

   private Button buttonLogOut;
    private Button buttonCheckNewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ctegory);

        buttonLogOut =findViewById(R.id.admin_logout_btn);
        buttonCheckNewOrders =findViewById(R.id.admin_new_orders_btn);

        hats=findViewById(R.id.hats);
        watches=findViewById(R.id.watches);
        bags=findViewById(R.id.bags);
        mobiles=findViewById(R.id.mobiles);
        laptops=findViewById(R.id.laptops);
        tShirts=findViewById(R.id.t_shirts);
        sportTshirts=findViewById(R.id.sports);
        shoes=findViewById(R.id.shoess);
        headphones=findViewById(R.id.headphoness);
        femaleDresses=findViewById(R.id.female_dresses);
        sweather=findViewById(R.id.sweather);
        glasses=findViewById(R.id.glasses);
        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });
        watches .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","watches");
                startActivity(intent);

            }
        });
        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","bags");
                startActivity(intent);

            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","mobiles");
                startActivity(intent);

            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","laptops");
                startActivity(intent);

            }
        });
        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);

            }
        });
        sportTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","sportTshirts");
                startActivity(intent);

            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","shoes");
                startActivity(intent);

            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","headphones");
                startActivity(intent);

            }
        });
        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","femaleDresses");
                startActivity(intent);

            }
        });
        sweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","sweather");
                startActivity(intent);

            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        buttonCheckNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCtegoryActivity.this,AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

    }
}
