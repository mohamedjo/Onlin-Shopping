package com.jo.android.onlineshopping;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.onlineshopping.model.Cart;
import com.jo.android.onlineshopping.viewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private DatabaseReference cartListRef;
    String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);
        recyclerView=findViewById(R.id.user_products_list);
        userId=getIntent().getStringExtra("uid");

        cartListRef= FirebaseDatabase.getInstance().getReference().child("cart list")
                .child("admin view").child(userId).child("products");

        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.textViewName.setText(model.getPname());
                holder.textViewPrice.setText(model.getPrice() + "$");
                holder.textViewQuantity.setText("Quantity = " + model.getQuantity());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);

                return holder;            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
