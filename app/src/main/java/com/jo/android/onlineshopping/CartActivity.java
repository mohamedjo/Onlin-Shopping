package com.jo.android.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jo.android.onlineshopping.model.Cart;
import com.jo.android.onlineshopping.prevalent.Prevalent;
import com.jo.android.onlineshopping.viewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button nextBtn;
    TextView textViewTotalPrice, textViewmsg1;
    int totalPrice=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        nextBtn = findViewById(R.id.btn_next);
        textViewTotalPrice = findViewById(R.id.tv_total_price);
        textViewmsg1=findViewById(R.id.tv_msg1);
        recyclerView = findViewById(R.id.cart_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTotalPrice.setText("total price="+totalPrice+"$");
                Intent intent=new Intent(CartActivity.this,ConfirFinalOrderActivity.class);
                intent.putExtra("total price",String.valueOf(totalPrice));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("user view").child(Prevalent.currentOnlineUser.getPhone())
                        .child("products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.textViewName.setText(model.getPname());
                holder.textViewPrice.setText(model.getPrice() + "$");
                holder.textViewQuantity.setText("Quantity = " + model.getQuantity());

                int oneItemPrice=Integer.valueOf(model.getPrice())*Integer.valueOf(model.getQuantity());
                totalPrice=totalPrice+oneItemPrice;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence charSequence[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);

                        builder.setTitle("Cart Option");

                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {

                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pID", model.getpID());
                                    startActivity(intent);
                                } else if (which == 1) {
                                    cartListRef.child("user view").child(Prevalent.currentOnlineUser.getPhone())
                                            .child("products").child(model.getpID())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "item removed successfully", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });


                                }

                            }
                        });
                        builder.show();

                    }
                });


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

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

                            textViewTotalPrice.setText("Dear "+userName+"order is shipped successfully");
                            recyclerView.setVisibility(View.GONE);
                            textViewmsg1.setVisibility(View.VISIBLE);
                            textViewmsg1.setText("congratolation your final order has been shipped successfully,soon it will  be receieved soon");
                            nextBtn.setVisibility(View.GONE);

                        }
                        else if(shippingState.equals("not shipped")){


                            textViewTotalPrice.setText("shipped state =not shipped");
                            recyclerView.setVisibility(View.GONE);
                            textViewmsg1.setVisibility(View.VISIBLE);
                            nextBtn.setVisibility(View.GONE);
                        }}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
