package com.jo.android.onlineshopping.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jo.android.onlineshopping.R;
import com.jo.android.onlineshopping.interfaces.ItemClickListner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewName,textViewPrice,textViewQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(View itemView) {
        super(itemView);

        textViewName=itemView.findViewById(R.id.cart_product_name);
        textViewPrice=itemView.findViewById(R.id.cart_product_price);
        textViewQuantity=itemView.findViewById(R.id.cart_product_quantity);

    }

    @Override
    public void onClick(View v) {

        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
