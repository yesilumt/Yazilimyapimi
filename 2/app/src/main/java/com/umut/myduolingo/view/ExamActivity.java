package com.umut.myduolingo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.umut.myduolingo.DataTemper.DataTemper;
import com.umut.myduolingo.adapter.FirestoreUtils;
import com.umut.myduolingo.databinding.ActivityExamBinding;
import com.umut.myduolingo.model.Question;

import java.util.ArrayList;
import java.util.Random;

public class ExamActivity extends AppCompatActivity {
    private ActivityExamBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    int forTrue,forFalse;
    int tekrarSayisi=10; // degisecek !!!
    int totalQuestion = tekrarSayisi;
    int soruSayisi=1;
    private FirestoreUtils firestoreUtils;
    private Question soru;
    int trueAnswerCounter=0;
    String gelenIntentData;
    private int countOfDoc;
    int randomNumber;
    int randomNumberW;
    Random random;
    ArrayList<String> documentPaths = new ArrayList<>();
    ArrayList<String> wrongWords  = new ArrayList<>();

    String trueAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityExamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        random = new Random();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        Intent gelenIntent =  getIntent();
        gelenIntentData=gelenIntent.getStringExtra("key");
        countOfDoc= Integer.parseInt(gelenIntentData);
        System.out.println("erer "+countOfDoc);
        tekrarSayisi = DataTemper.questionCount;
        totalQuestion = tekrarSayisi;
        binding.trueAnswerCounterBtn.setText("Score:"+trueAnswerCounter+"/"+totalQuestion);
        flow();
    }


    public void flow(){
        binding.answer1.setBackgroundColor(Color.BLUE);
        binding.answer2.setBackgroundColor(Color.BLUE);
        if (tekrarSayisi>0){
            randomNum();
            if (randomNumber!=randomNumberW){
                getQuestion(randomNum());
                // getQuestion(2); //test
                binding.questionCounter.setText("Kalan soru sayisi "+tekrarSayisi);
            }
            else{
                flow();
            }
            tekrarSayisi=tekrarSayisi-1;
        }
        else{
            binding.answer1.setEnabled(false);
            binding.answer2.setEnabled(false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(ExamActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2500);
            //
            //sonuc aktıvıtesıne gıdılecek
        }
    }


    public void getQuestion(int arananId){
        FirestoreUtils.getDataForId(arananId, new FirestoreUtils.OnDataFetchListener() {
            @Override
            public void onSuccess(String downloadUrl, String english, String means, String turkish, String email, long id, ArrayList<String> wrongAnswers) {
                binding.Question.setText(english);
                //binding.answer1.setText(turkish);
                ArrayList<String> words = new ArrayList<>();
                trueAnswer = turkish;
                words.add(turkish);
                words.add(wrongAnswers.get(random.nextInt(wrongAnswers.size()-1)));
                int firstWordIndex = random.nextInt(2);
                binding.answer1.setText(words.get(firstWordIndex));
                words.remove(firstWordIndex);
                binding.answer2.setText(words.get(0));
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ExamActivity.this, errorMessage.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public int randomNum(){

        randomNumber = random.nextInt(countOfDoc) + 1;
        return randomNumber;
    }




    public void answerBtn(View view) {
       Button btn = findViewById(view.getId());
       String word = btn.getText().toString();

       if(trueAnswer.equals(word)){
           btn.setBackgroundColor(Color.GREEN);
           Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   btn.setBackgroundColor(Color.BLUE);
                   flow();
                   trueAnswerCounter=trueAnswerCounter+1;
                   binding.trueAnswerCounterBtn.setText("Score:"+trueAnswerCounter+"/"+totalQuestion);
               }
           }, 500);
           System.out.println("Dogruuuuu");
       }
       else{
           btn.setBackgroundColor(Color.RED);
           Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   btn.setBackgroundColor(Color.BLUE);
                   flow();
               }
           }, 500);
       }
    }

}