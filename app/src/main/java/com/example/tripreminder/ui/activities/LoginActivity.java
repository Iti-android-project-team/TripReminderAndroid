package com.example.tripreminder.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tripreminder.R;
import com.example.tripreminder.ui.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button  signIn;
    EditText editEmail , editPassword;
    TextView register;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       initialize();
       /* if (fAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Register.class));
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String sEmail=  editEmail.getText().toString();
                String sPassword=   editPassword.getText().toString();
                if (! sEmail.equals("")&& ! sPassword.equals("")) {
                    fAuth.signInWithEmailAndPassword(sEmail, sPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(LoginActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Enter Your Email and Password",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        });
    }
    public void initialize(){
        signIn = findViewById(R.id.btn_log_in);
        editEmail = findViewById(R.id.email_edit);
        editPassword = findViewById(R.id.pass_edit);
        register = findViewById(R.id.text_register);
        fAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Signing In please wait...");

    }
}