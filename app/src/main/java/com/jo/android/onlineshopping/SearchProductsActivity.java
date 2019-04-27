package com.jo.android.onlineshopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

public class SearchProductsActivity extends AppCompatActivity {

    EditText editTextSearch;
    Button buttonSearch;
    RecyclerView recyclerViewSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        editTextSearch=findViewById(R.id.search_products);
        buttonSearch=findViewById(R.id.button_search);
        recyclerViewSearch=findViewById(R.id.search_list);
    }
}
