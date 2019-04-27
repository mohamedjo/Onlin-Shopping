package com.jo.android.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.onlineshopping.model.Product;
import com.jo.android.onlineshopping.prevalent.Prevalent;
import com.jo.android.onlineshopping.viewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


   private DatabaseReference productsRef;

   private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        productsRef= FirebaseDatabase.getInstance().getReference().child("products");

        Paper.init(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navigationHeader=navigationView.getHeaderView(0);
        TextView userNameTextView=navigationHeader.findViewById(R.id.user_name);
        CircleImageView profileImageView=navigationHeader.findViewById(R.id.profile_image);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());

        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        recyclerView=findViewById( R.id.resycler_menu);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product>options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productsRef,Product.class)
                .build();

        FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Product model) {

                holder.textViewName.setText(model.getpName());
                holder.textViewPrice.setText(model.getPrice());
                holder.textViewDescription.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.productImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(HomeActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("pID",model.getpID());
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                ProductViewHolder productViewHolder=new ProductViewHolder(view);
                return productViewHolder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent=new Intent(HomeActivity.this,SearchProductsActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_setting) {
            Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();

            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
