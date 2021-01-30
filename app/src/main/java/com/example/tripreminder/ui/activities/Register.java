package com.example.tripreminder.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    public static final String TAG = "tag";
    Button signup;
    EditText editEmail, editPassword, editConfirmPassword;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase fDatabase;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
      initialize();
     /* if (fAuth.getCurrentUser()!= null){
          startActivity(new Intent(getApplicationContext(),MainActivity.class));
          finish();
      }*/
      signup.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String email = editEmail.getText().toString().trim();
              String passward = editPassword.getText().toString().trim();
              String confirmPassword = editConfirmPassword.getText().toString().trim();
              if (TextUtils.isEmpty(email)){
                  editEmail.setError("Email is Required.");
                  return;
              }
              if (TextUtils.isEmpty(passward)){
                  editPassword.setError("Password is Required.");
                  return;
              }
              if (TextUtils.isEmpty(confirmPassword)){
                  editConfirmPassword.setError("ConfirmPassword is Required.");
                  return;
              }
              if (!passward.equals(confirmPassword)){
                  editConfirmPassword.setError("ConfirmPassword not Equal Password");
                  return;
              }
              fAuth.createUserWithEmailAndPassword(email,passward).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      progressDialog.show();
                      if (task.isSuccessful()){
                          Toast.makeText(Register.this, "User Created!", Toast.LENGTH_LONG).show();
                           userID = fAuth.getCurrentUser().getUid();
                          reference =fDatabase.getReference().child("users").child(userID);
                          User userData = new User(email,passward);
                           reference.setValue(userData);
                         /* DocumentReference documentReference =fStore.collection("users").document(userID);
                          Map<String,Object> user = new HashMap<>();
                          user.put("Email",email);
                          user.put("password",passward);
                          documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void aVoid) {
                                  Log.d(TAG,"success"+userID);
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Log.d(TAG,"fail "+e.toString());
                              }
                          });*/
                          startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                          progressDialog.dismiss();
                      }else {
                          Toast.makeText(Register.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                          progressDialog.dismiss();
                      }
                  }
              });
          }
      });
    }



    public void initialize(){
        editEmail = findViewById(R.id.edit_email_signup);
        editPassword = findViewById(R.id.edit_password_sign_up);
        editConfirmPassword = findViewById(R.id.edit_confirm_password_sign_up);
        signup = findViewById(R.id.button_sign_up);
        fAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Signing up please wait...");
    }
}