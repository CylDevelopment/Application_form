package com.example.saral_suvidha.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saral_suvidha.activities.MainActivity;
import com.example.saral_suvidha.R;
import com.example.saral_suvidha.helper.SessionManagement;
import com.example.saral_suvidha.helper.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    TextView newUserSignUp, forgotPassword;
    EditText txtEmail, txtPassword;
    ImageView imgFacebook, imgGoogle, imgTwitter;
    private FirebaseAuth firebaseAuth;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.login_email);
        txtPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);
        forgotPassword = findViewById(R.id.txt_forgot_password);
        imgFacebook = findViewById(R.id.login_facebook_toggle);
        imgGoogle = findViewById(R.id.login_google_toggle);
        imgTwitter = findViewById(R.id.login_twitter_toggle);

        firebaseAuth = FirebaseAuth.getInstance();

        newUserSignUp = findViewById(R.id.login_txt_create_account);
        newUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = txtEmail.getText().toString().trim();
                final String password = txtPassword.getText().toString().trim();

                EditText[] editTexts = {txtEmail, txtPassword};

                final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).build();
                waitingDialog.show();
                waitingDialog.setTitle("Please Wait");
                waitingDialog.setMessage("Authenticating.....");
                waitingDialog.setCancelable(false);

                if(new Validator(LoginActivity.this).validateEditText(editTexts)){
                    if (password.length() < 8) {
                        waitingDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Password length should be > 8", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    waitingDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                                        sessionManagement.createSession("",password,email);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Failed ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    waitingDialog.dismiss();
                }

            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Currently Not Available", Toast.LENGTH_SHORT).show();
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Currently Not Available", Toast.LENGTH_SHORT).show();
            }
        });

        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Currently Not Available", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
