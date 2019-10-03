package com.example.saral_suvidha.UserProfile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saral_suvidha.Authentication.RegisterDetailActivity;
import com.example.saral_suvidha.R;
import com.example.saral_suvidha.helper.Validator;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfileSettings extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference mStorageReference;

    TextInputEditText userName, userEmail, userPhone;
    Button btnUpdate;
    CircleImageView imgUser;

    ProgressDialog mProgressDialog;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_settings);

        userName = findViewById(R.id.edtTxtName_EdtProSettings);
        userEmail = findViewById(R.id.edtTxtEmail_EdtProSettings);
        imgUser = findViewById(R.id.img_EditProfileSettings);
        userPhone = findViewById(R.id.edtTxtPhone_EdtProSettings);
        btnUpdate = findViewById(R.id.update_profile);

        userEmail.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mStorageReference = FirebaseStorage.getInstance().getReference("profile_images");
        getData(firebaseUser);

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = userName.getText().toString().trim();
                String phone = userPhone.getText().toString().trim();

                final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(EditProfileSettings.this).build();
                waitingDialog.show();
                waitingDialog.setTitle("Please Wait");
                waitingDialog.setMessage("Updating");

                EditText[] editTexts ={userName, userEmail, userPhone};

                if(new Validator(EditProfileSettings.this).validateEditText(editTexts)){

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("phone", phone);

                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            waitingDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(EditProfileSettings.this, ProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(intent);
                                Toast.makeText(EditProfileSettings.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EditProfileSettings.this, "Error Uploading Details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    waitingDialog.dismiss();
                }
            }
        });

    }

    private void getData(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String img = dataSnapshot.child("image").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();

                if(img.equals("default")){
                    imgUser.setImageDrawable(getResources().getDrawable(R.drawable.register_user_dummy));
                }else{
                    Picasso.get().load(img).into(imgUser);
                }
                userName.setText(name);
                userEmail.setText(email);
                userPhone.setText(phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();

            CropImage.activity(imgUri)
                    .setAspectRatio(1, 1)
                    .start(EditProfileSettings.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(EditProfileSettings.this);
                mProgressDialog.setTitle("Uploading Image");
                mProgressDialog.setMessage("Please Wait...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                if (resultUri == null) {
                    Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    return;
                }


                final StorageReference filePath = mStorageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        mProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String url = task.getResult().toString();
                            databaseReference.child("image").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileSettings.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditProfileSettings.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {

                        }

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }
}