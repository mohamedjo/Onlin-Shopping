package com.jo.android.onlineshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.jo.android.onlineshopping.prevalent.Prevalent;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextPhoneNum, editTextFullName, editTextAddress;
    TextView textViewClose, textViewUpdate, textViewChangeImage;
    CircleImageView imageViewProfile;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editTextAddress = findViewById(R.id.edit_text_setting_adress);
        editTextFullName = findViewById(R.id.edit_text_setting_full_name);
        editTextPhoneNum = findViewById(R.id.edit_text_setting_phone_number);
        textViewClose = findViewById(R.id.close_setting_btn);
        textViewUpdate = findViewById(R.id.update_setting_btn);
        textViewChangeImage = findViewById(R.id.change_profile_image_btn);
        imageViewProfile = findViewById(R.id.setting_profile_image);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
      displayUserInfo(imageViewProfile,editTextPhoneNum,editTextAddress,editTextFullName);
    }



    private void displayUserInfo(final CircleImageView imageViewProfile, final EditText editTextPhoneNum, final EditText editTextAddress, final EditText editTextFullName) {

        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getPhone());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){

                   if(dataSnapshot.child("image").exists()){

                       String phone=dataSnapshot.child("phone").getValue().toString();
                       String image=dataSnapshot.child("image").getValue().toString();
                       String address=dataSnapshot.child("address").getValue().toString();
                       String name=dataSnapshot.child("name").getValue().toString();

                       Picasso.get().load(image).into(imageViewProfile);
                       editTextAddress.setText(address);
                       editTextPhoneNum.setText(phone);
                       editTextFullName.setText(name);

                   }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();

                }
                else{
                    updateOnlyUserInfo();


                }
            }
        });

        textViewChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK&&data!=null){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);

            imageUri=result.getUri();
            imageViewProfile.setImageURI(imageUri);

        }
        else{
            Toast.makeText(this, "Error try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();

                    }
    }
    private void userInfoSaved() {
        if (TextUtils.isEmpty(editTextFullName.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(editTextAddress.getText().toString()))
        {
            Toast.makeText(this, "Address is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(editTextPhoneNum.getText().toString()))
        {
            Toast.makeText(this, "phone is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){

            final StorageReference fileRef = storageProfilePrictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }) .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {

                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("name", editTextFullName.getText().toString());
                        userMap. put("address", editTextAddress.getText().toString());
                        userMap. put("phone", editTextPhoneNum.getText().toString());
                        userMap. put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "image is not selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", editTextFullName.getText().toString());
        userMap. put("address", editTextAddress.getText().toString());
        userMap. put("phone", editTextPhoneNum.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }
}