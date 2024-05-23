package com.umut.myduolingo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;
import com.umut.myduolingo.DataTemper.DataTemper;
import com.umut.myduolingo.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseAuth auth;
    int tekrarSayisi=10;
    String tekrarSayisiString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        tekrarSayisi=DataTemper.questionCount;
        binding.questionCountTxt.setText(""+tekrarSayisi);



        String email =auth.getCurrentUser().getEmail().toString();
        binding.userMail.setText(email);
        binding.questionBar.setProgress(DataTemper.questionCount/5);
        binding.questionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tekrarSayisi=progress*5;
                tekrarSayisiString= String.valueOf(tekrarSayisi);
                System.out.println(tekrarSayisi);
                DataTemper.questionCount = progress*5;
                binding.questionCountTxt.setText(""+tekrarSayisi);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void goProfile(MenuItem item){

    }

    public void logOutFun(View view) {

        auth.signOut();
        Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void goHome(MenuItem item){
        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }



}