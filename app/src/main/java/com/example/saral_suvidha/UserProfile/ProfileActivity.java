package com.example.saral_suvidha.UserProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saral_suvidha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    TextView txtName, txtEmail, txtPhone, userInfo;
    Button btnUpdateProfile;
    CircleImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = findViewById(R.id.img_profileActivity);
        userInfo = findViewById(R.id.profileActivity_userinfo);
        txtEmail = findViewById(R.id.txtEmail_current_user_profile);
        txtName = findViewById(R.id.txtName_current_user_profile);
        txtPhone = findViewById(R.id.txtPhone_current_user_profile);
        btnUpdateProfile = findViewById(R.id.button_update_profile);


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileSettings.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String current_UID = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(current_UID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String img = dataSnapshot.child("image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();

                if(img.equals("default")){
                    imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.register_user_dummy));
                }else{
                    Picasso.get().load(img).into(imgProfile);
                }

                userInfo.setText(name+"'s Profile");
                txtEmail.setText(email);
                txtName.setText(name);
                txtPhone.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error Fetching User Details", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
