package com.jo.android.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.onlineshopping.model.Product;
import com.jo.android.onlineshopping.prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView imageViewProduct;
    TextView textViewName,textViewDescription,textViewPrice;
    ElegantNumberButton elegantNumberButton;
    Button buttonAddToCart;
    String productId="";
    String state="normal";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productId=getIntent().getStringExtra("pID");
        Toast.makeText(this, productId, Toast.LENGTH_SHORT).show();
        imageViewProduct=findViewById(R.id.product_image_det) ;
        textViewName=findViewById(R.id.product_name_det) ;
        textViewDescription=findViewById(R.id.product_description_det) ;
        textViewPrice=findViewById(R.id.product_price_det);
        elegantNumberButton=findViewById(R.id.elegant_btn);
        buttonAddToCart=findViewById(R.id.btn_add_product_to_cart);
       getProductDetails(productId);

       buttonAddToCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(state.equals("shipped")||state.equals("not shipped")){

                   Toast.makeText(ProductDetailsActivity.this, "you can add mor product once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
               }
               else{
                   addingToCartList();

               }
           }
       });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private void addingToCartList() {

        String saveTime,saveDate;
        Calendar calendarForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveDate=currentDate.format(calendarForDate.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveTime=currentTime.format(calendarForDate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("cart list");
        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pID",productId);
        cartMap.put("date",saveDate);
        cartMap.put("time",saveTime);
        cartMap.put("pname",textViewName.getText().toString());
        cartMap.put("price",textViewPrice.getText().toString());
        cartMap.put("quantity",elegantNumberButton.getNumber());
        cartMap.put("discount","");
        cartListRef.child("user view").child(Prevalent.currentOnlineUser.getPhone())
                .child("products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            cartListRef.child("admin view").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, " added to cart", Toast.LENGTH_SHORT).show();

                                                Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    private void getProductDetails(final String productId) {
        DatabaseReference productsReference= FirebaseDatabase.getInstance().getReference().child("products");

        productsReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Product product=dataSnapshot.getValue(Product.class);
                    textViewName.setText(product.getpName());
                    textViewDescription.setText(product.getDescription());
                    textViewPrice.setText(product.getPrice());
                    Picasso.get().load(product.getImage()).into(imageViewProduct);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void checkOrderState(){
        DatabaseReference ordersRef=FirebaseDatabase.getInstance().getReference().child("orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String shippingState=dataSnapshot.child("state").getValue().toString();
                    String userName=dataSnapshot.child("name").getValue().toString();
                    if(dataSnapshot.exists()){

                        if(shippingState.equals("shipped")){


                            state="shipped";
                        }
                        else if(shippingState.equals("not shipped")){
                            state="not shipped";

                        }}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
