package com.example.saral_suvidha.Authentication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saral_suvidha.R;
import com.example.saral_suvidha.helper.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    TextView alreadyUserLogin;
    EditText txtEmail, txtPassword, txtConfirmPassword;
    Button btnRegister;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        txtEmail = findViewById(R.id.register_email);
        txtPassword = findViewById(R.id.register_password);
        txtConfirmPassword = findViewById(R.id.register_confirm_password);
        btnRegister = findViewById(R.id.btn_register);


        alreadyUserLogin = findViewById(R.id.register_txt_sign_in);
        alreadyUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String cnfmPassword = txtConfirmPassword.getText().toString().trim();

                EditText[] editTexts = {txtEmail, txtPassword};

                final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).build();
                waitingDialog.show();
                waitingDialog.setMessage("..Please Wait..");
                waitingDialog.setCancelable(false);

                if (new Validator(RegisterActivity.this).validateEditText(editTexts)){
                    if (password.length() < 8) {
                        waitingDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Password length should be > 8", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.equals(cnfmPassword)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        waitingDialog.dismiss();
                                        if (task.isSuccessful()) {

                                            HashMap<String, Object> userMap = new HashMap<>();
                                            userMap.put("uid",firebaseAuth.getCurrentUser().getUid());
                                            userMap.put("name", "default");
                                            userMap.put("phone", "default");
                                            userMap.put("email", firebaseAuth.getCurrentUser().getEmail());
                                            userMap.put("image", "default");
                                            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).
                                                    setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(getApplicationContext(), RegisterDetailActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        } else {
                                            Toast.makeText(RegisterActivity.this, "User Registration Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        waitingDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    waitingDialog.dismiss();
                }
            }
        });

    }
}
