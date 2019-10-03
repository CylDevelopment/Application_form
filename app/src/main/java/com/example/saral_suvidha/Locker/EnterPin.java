package com.example.saral_suvidha.Locker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.saral_suvidha.R;
import com.example.saral_suvidha.helper.SessionManagement;
import com.goodiebag.pinview.Pinview;

import java.util.HashMap;

public class EnterPin extends AppCompatActivity {

    Pinview pin;
    String pinPass;
    String pinPassword;
    Button btnEnterPin;

    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);


        sessionManagement = new SessionManagement(this);
        HashMap<String, String> map = sessionManagement.getPin();
        final String myPin = map.get("pin");

        pin = findViewById(R.id.pinView_enter_pin);
        btnEnterPin = findViewById(R.id.btnEnterPin);

        pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                pinPass = pinview.getValue();
            }
        });

        btnEnterPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pinPass.equals(myPin)){
                    Intent intent = new Intent(EnterPin.this, UserData.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(EnterPin.this, "Incorrect Pin", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
