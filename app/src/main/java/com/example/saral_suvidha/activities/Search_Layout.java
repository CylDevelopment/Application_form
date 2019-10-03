package com.example.saral_suvidha.activities;

import android.annotation.TargetApi;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.example.saral_suvidha.R;

public class Search_Layout extends AppCompatActivity {
    Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window.getDefaultFeatures(this);
        setContentView(R.layout.activity_search__layout);

        toolbar = findViewById(R.id.toolbar_search);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Search_Layout.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });

    }


}
