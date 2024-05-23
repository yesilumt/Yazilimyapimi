package com.umut.myduolingo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.umut.myduolingo.databinding.ActivityUpdatePassswordBinding;

public class UpdatePasswordActivity extends AppCompatActivity {
    ActivityUpdatePassswordBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePassswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();


    }

    public void updatePassword(View view) {


            if(binding.signupEmail.getText().toString().isEmpty()){

                Toast.makeText(UpdatePasswordActivity.this,"Bir Mail Adresi Giriniz!",Toast.LENGTH_SHORT).show();

            }else{

                auth.sendPasswordResetEmail(binding.signupEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(UpdatePasswordActivity.this,"Sıfırlama Maili Gönderildi!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdatePasswordActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }else{

                            Log.e("hata",task.getException().toString());

                        }

                    }
                });


        }
    }
}