package com.example.saral_suvidha.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saral_suvidha.R;
import com.example.saral_suvidha.helper.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class ForgotPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText txtUserEmail;
    Button btnForgotPass;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = findViewById(R.id.toolbar_forgot_password_activity);
        txtUserEmail = findViewById(R.id.forgotPassword_email);
        btnForgotPass = findViewById(R.id.btn_resetPass);

        toolbar.setTitle("Reset Your Password...");

        firebaseAuth = FirebaseAuth.getInstance();

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtUserEmail.getText().toString().trim();
                EditText[] editTexts = {txtUserEmail};

                final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(ForgotPasswordActivity.this).build();
                waitingDialog.show();
                waitingDialog.setMessage("..Please Wait..");

                if(new Validator(ForgotPasswordActivity.this).validateEditText(editTexts)){
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            waitingDialog.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this, "Reset Link Send to Your Email..", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(ForgotPasswordActivity.this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
