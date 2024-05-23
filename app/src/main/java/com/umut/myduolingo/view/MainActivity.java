package com.umut.myduolingo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.umut.myduolingo.R;
import com.umut.myduolingo.adapter.FirestoreUtils;
import com.umut.myduolingo.databinding.ActivityMainBinding;

public class  MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseAuth auth;
    ImageView examView;
    ImageView dictionaryView;
    int countOfDoc ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        examView = findViewById(R.id.examView);
        dictionaryView = findViewById(R.id.dictionaryView);

        auth = FirebaseAuth.getInstance();


        FirebaseUser user = auth.getCurrentUser();
        if (user==null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }

        binding.examView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDocumentCount();

            }
        });

        binding.dictionaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void goProfile(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void goHome(MenuItem item){

    }

    public void goUploadScreen(View view) {

        Intent intent = new Intent(MainActivity.this, UploadScreenActivity.class);
        startActivity(intent);
        finish();
    }

    public int getDocumentCount(){
        FirestoreUtils.getDocumentCount("Posts", new FirestoreUtils.OnCountCompleteListener() {
            @Override
            public void onComplete(long count) {
                countOfDoc=(int) count;
                String countOfDocString = String.valueOf(countOfDoc);
                Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                intent.putExtra("key",countOfDocString);
                startActivity(intent);
            }
        });

        return countOfDoc;
    }


}