package com.example.saral_suvidha;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class SaralSuvidha extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
