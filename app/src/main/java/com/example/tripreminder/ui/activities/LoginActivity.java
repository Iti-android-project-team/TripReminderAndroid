package com.example.tripreminder.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tripreminder.R;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.ui.activities.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

import com.example.tripreminder.ui.activities.addTrip.AddTripActivity;

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
    GoogleSignInClient mGoogleSignInClient;
   int RC_SIGN_IN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

       initialize();
//        if (fAuth.getCurrentUser()!= null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Register.class));
                finish();
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
                                        SharedPref.setLogin(true);
                                        SharedPref.setUserEmail(sEmail);
                                        Log.e("le",sEmail);
                                        finish();
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
        SignInButton signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    public void initialize(){
        signIn = findViewById(R.id.btn_log_in);
        editEmail = findViewById(R.id.email_edit);
        editPassword = findViewById(R.id.pass_edit);
        register = findViewById(R.id.text_register);
        fAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Signing In please wait...");
        SharedPref.createPrefObject(LoginActivity.this);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void updateUI(GoogleSignInAccount account){
        if (account != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }  else {
            Toast.makeText(this, "Please login with a valid Google account", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);
        }
    }
}