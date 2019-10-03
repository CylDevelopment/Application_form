package com.example.saral_suvidha.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saral_suvidha.R;
import com.example.saral_suvidha.UserProfile.EditProfileSettings;

public class Settings extends AppCompatActivity {

    LinearLayout editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editProfile = findViewById(R.id.edit_profile_settings);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Settings.this, EditProfileSettings.class);
                startActivity(intent);

            }
        });

    }
}
