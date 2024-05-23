package com.umut.myduolingo.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.umut.myduolingo.adapter.PostAdapter;
import com.umut.myduolingo.databinding.ActivityDictionaryScreenBinding;
import com.umut.myduolingo.model.Post;

import java.util.ArrayList;
import java.util.Map;

public class DictionaryActivity extends AppCompatActivity {
    private ActivityDictionaryScreenBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    PostAdapter postAdapter;

    ArrayList<Post> postArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<>();


        getData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.recyclerView.setAdapter(postAdapter);

    }

    private void getData(){
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!= null){
                    Toast.makeText(DictionaryActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }if (value!=null){
                    for (DocumentSnapshot snapshot: value.getDocuments()){
                        Map<String, Object> data = snapshot.getData();
                        String userEmail = (String) data.get("email");
                        String engWord = (String) data.get("English");
                        String mean = (String) data.get("Means");
                        String trWord = (String) data.get("Turkish");
                        String downloadUrl = (String) data.get("DownloadUrl");
                        System.out.println(mean);

                        Post post = new Post(userEmail,mean,trWord,engWord,downloadUrl);
                        postArrayList.add(post);

                    }
                    postAdapter.notifyDataSetChanged();
                }
            }
        });

    }


    public void goHomeClc(View view) {
        Intent i = new Intent(DictionaryActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }
}