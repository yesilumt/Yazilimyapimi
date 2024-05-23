package com.umut.myduolingo.adapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirestoreUtils {

    public static void getDataForId(int id, final OnDataFetchListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Posts");

        Query query = collectionRef.whereEqualTo("id", id);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Sorgu sonuçlarını döngüyle işle
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Belgeyi al ve dinleyiciye ile
                    String downloadUrl = documentSnapshot.getString("DownloadUrl");
                    String english = documentSnapshot.getString("English");
                    String means = documentSnapshot.getString("Means");
                    String turkish = documentSnapshot.getString("Turkish");
                    String email = documentSnapshot.getString("email");
                    long id = documentSnapshot.getLong("id");
                    ArrayList<String> wrongAnswers = (ArrayList<String>) documentSnapshot.get("wrongAnswers");

                    listener.onSuccess(downloadUrl, english, means, turkish, email, id, wrongAnswers);
                    return;
                }
                listener.onFailure("Belge bulunamadı");
            }
        });
    }


    public interface OnDataFetchListener {
        void onSuccess(String downloadUrl, String english, String means, String turkish, String email, long id, ArrayList<String> wrongAnswers);
        void onFailure(String errorMessage);
    }

    public interface OnCountCompleteListener {
        void onComplete(long count);
    }

    public static void getDocumentCount(String collectionPath, final OnCountCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection(collectionPath);
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (listener != null) {
                    listener.onComplete(queryDocumentSnapshots.size());
                }
            }
        });
    }

}
