package com.jo.android.onlineshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProductActivity extends AppCompatActivity {
    ImageView selectpeoductImage;
    EditText selectProductName;
    EditText selectProductDescription;
    EditText selectProductPrice;
    Button addNewProductButton;
    String name,description,price;
    String saveCurrentDate,saveCurrentTime,productRandomKey,downloadImageUrl;
    String category;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    ProgressDialog loadingBar;


    private Uri imageUri;

    private static final int PICK_IMAGE=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);
         category=getIntent().getStringExtra("category");
        productImagesRef= FirebaseStorage.getInstance().getReference().child("product images");
        productsRef= FirebaseDatabase.getInstance().getReference().child("products");
        selectpeoductImage=findViewById(R.id.iv_add_new_product);
        selectProductName=findViewById(R.id.select_product_name);
        selectProductDescription=findViewById(R.id.select_product_description);
        selectProductPrice=findViewById(R.id.select_product_price);
        loadingBar=new ProgressDialog(this);

        addNewProductButton=findViewById(R.id.button_add_new_product);
        selectpeoductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGaliry();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

    }

    private void validateProductData() {
        
        name=selectProductName.getText().toString();
        description=selectProductDescription.getText().toString();
        price=selectProductPrice.getText().toString();
        
        if(imageUri==null){
            Toast.makeText(this, "please add the product image", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "please add the product name", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "please add the product description", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "please add the product price", Toast.LENGTH_SHORT).show();
        }
        else {

            storeProductInformation();

        }
    }

    private void storeProductInformation() {
        loadingBar.setTitle("login account");
        loadingBar.setMessage("please wait, while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;
        final StorageReference filePath=productImagesRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loadingBar.dismiss();

                                                Toast.makeText(AdminAddProductActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
        ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddProductActivity.this, "image uploaded correctly", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl=task.getResult().toString();
                        Toast.makeText(AdminAddProductActivity.this, "got the product Image Url successfully", Toast.LENGTH_SHORT).show();

                        saveProductInfoToDataBase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDataBase() {
        HashMap<String,Object> productInfo=new HashMap<>();

        productInfo.put("pID",productRandomKey);
        productInfo.put("date",saveCurrentDate);
        productInfo.put("time",saveCurrentTime);
        productInfo.put("description",description);
        productInfo.put("pName",name);
        productInfo.put("price",price);
        productInfo.put("image",downloadImageUrl);
        productInfo.put("category",category);

        productsRef.child(productRandomKey).updateChildren(productInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                  Intent intent = new Intent(AdminAddProductActivity.this, AdminCtegoryActivity.class);
                  startActivity(intent);
                  loadingBar.dismiss();


                  Toast.makeText(AdminAddProductActivity.this, "the product  uploaded to the dataBase Successfully", Toast.LENGTH_SHORT).show();
              }
              else {
                  loadingBar.dismiss();

                  Toast.makeText(AdminAddProductActivity.this, "error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
              }
            }
        });

    }

    private void openGaliry() {
        Intent intentGliry=new Intent();
        intentGliry.setAction(Intent.ACTION_GET_CONTENT);
        intentGliry.setType("image/*");
        startActivityForResult(intentGliry,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE&&resultCode==RESULT_OK&&data!=null){

            imageUri=data.getData();
            selectpeoductImage.setImageURI(imageUri);



        }
    }
}
