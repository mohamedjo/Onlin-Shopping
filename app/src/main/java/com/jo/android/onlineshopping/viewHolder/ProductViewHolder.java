package com.jo.android.onlineshopping.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jo.android.onlineshopping.R;
import com.jo.android.onlineshopping.interfaces.ItemClickListner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   public TextView textViewName,textViewPrice,textViewDescription;
    public ImageView productImage;
    ItemClickListner listner;
    public ProductViewHolder(View itemView) {
        super(itemView);

        textViewName=itemView.findViewById(R.id.tv_product_name);
        textViewPrice=itemView.findViewById(R.id.tv_product_price);
        textViewDescription=itemView.findViewById(R.id.tv_product_discription);
        productImage=itemView.findViewById(R.id.product_image);


    }

    public void setItemClickListener(ItemClickListner listner){

        this.listner=listner;
    }

    @Override
    public void onClick(View v) {

        listner.onClick(v,getAdapterPosition(),false);
    }
}
