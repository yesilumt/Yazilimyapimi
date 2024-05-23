package com.umut.myduolingo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.umut.myduolingo.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();


        FirebaseUser user = auth.getCurrentUser();
        if (user!=null){
            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }
    }


    public void SignupBtn(View view) {
        String email =binding.signupEmail.getText().toString();
        String password =binding.signupPassword.getText().toString();
        String Confirm_password =binding.signupConfirm.getText().toString();
        if (!email.equals("")||!password.equals("")||!Confirm_password.equals("")) {
            if (password.equals(Confirm_password)) {
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignupActivity.this, "Basarili", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else
                Toast.makeText(SignupActivity.this, "Şifreler aynı degil", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(SignupActivity.this, "Boslukları doldurunuz", Toast.LENGTH_LONG).show();

    }

    public void goLoginfromSignin(View view) {
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}