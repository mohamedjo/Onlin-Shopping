package com.jo.android.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.onlineshopping.model.AdminOrder;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
        recyclerView = findViewById(R.id.orders_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrder> options = new FirebaseRecyclerOptions.Builder<AdminOrder>()
                .setQuery(ordersRef, AdminOrder.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrder, AdminOrderViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrder, AdminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final AdminOrder model) {
                holder.textViewName.setText("name: " + model.getName());
                holder.textViewPhone.setText("phone: " + model.getPhone());
                holder.textViewAddrssShipping.setText("shipped address:" + model.getAddress() + ", " + model.getCity());
                holder.textViewDatTime.setText("order at:" + model.getDate() + " " + model.getTime());
                holder.textViewTotalAmount.setText("total amount: " + model.getTottalAmount());

                holder.buttonShowOrderProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid", userId);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Yes", "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("have you shipped this order products ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0) {
                                    String userId = getRef(position).getKey();

                                    removeOrder(userId);

                                } else if (which == 1) {
                                    finish();


                                }
                            }
                        });
                        builder.show();

                    }
                });


            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item_layout, parent, false);

                AdminOrderViewHolder adminOrderViewHolder = new AdminOrderViewHolder(view);

                return adminOrderViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void removeOrder(String userId) {
        ordersRef.child(userId).removeValue();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewPhone, textViewAddrssShipping, textViewDatTime, textViewTotalAmount;
        Button buttonShowOrderProducts;

        public AdminOrderViewHolder(View itemView) {

            super(itemView);
            textViewName = itemView.findViewById(R.id.order_user_name);
            textViewPhone = itemView.findViewById(R.id.order_user_phone);
            textViewAddrssShipping = itemView.findViewById(R.id.order_address_city);
            textViewDatTime = itemView.findViewById(R.id.order_date_time);
            textViewTotalAmount = itemView.findViewById(R.id.order_total_price);
            buttonShowOrderProducts = itemView.findViewById(R.id.btn_show_order_products);

        }
    }
}
