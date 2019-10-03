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

public class PinCreation extends AppCompatActivity {

    Pinview pin, confmPass;
    String pass, cnfmPass;
    Button btnSubmitPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_creation);

        pin = findViewById(R.id.pinView_create_pin);
        confmPass = findViewById(R.id.pinView_confirm_pin);
        btnSubmitPin = findViewById(R.id.btnPinSubmit);

        pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                pass = pinview.getValue();
            }
        });

        confmPass.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                cnfmPass = pinview.getValue();
            }
        });

        btnSubmitPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pass.equals("") || cnfmPass.equals("")){
                    Toast.makeText(PinCreation.this, "Feilds Can't be Empty", Toast.LENGTH_SHORT).show();
                }

                if(pass.equals(cnfmPass)){

                    SessionManagement sessionManagement = new SessionManagement(PinCreation.this);
                    sessionManagement.createPinNew(pass);

                    Intent intent = new Intent(PinCreation.this, UserData.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(PinCreation.this, "Pin doesn't match", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
