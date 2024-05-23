package com.umut.myduolingo.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.umut.myduolingo.databinding.ActivityUploadScreenBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UploadScreenActivity extends AppCompatActivity {
    private ActivityUploadScreenBinding binding;
    private FirebaseAuth auth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    Uri imageData;
    Bitmap selectedImage;
    int countDoc;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    private void getDocumentCount(String collectionName, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        CollectionReference collectionReference = db.collection(collectionName);
        collectionReference.get().addOnCompleteListener(onCompleteListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        String collectionName = "Posts";

        OnCompleteListener<QuerySnapshot> onCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int documentCount = task.getResult().size();
                    countDoc=documentCount;
                } else {
                    Log.d("Document Count", "Error getting documents: ", task.getException());
                }
            }
        };

        getDocumentCount(collectionName, onCompleteListener);



        registerLauncher();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

    }

    public void uploadBtnClick(View view) {
        UUID uuid = UUID.randomUUID();
        String imageName = "images/"+uuid+".jpg";


        if (imageData!=null){
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReferance = firebaseStorage.getReference(imageName);
                    newReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();
                            String EngWord=binding.engWord.getText().toString();
                            String TrWord=binding.trWord.getText().toString();
                            String Means= binding.mean.getText().toString();
                            FirebaseUser user =auth.getCurrentUser();
                            String email= user.getEmail();
                            ArrayList<String> wrongAnswers = new ArrayList<>();
                            wrongAnswers.add("Kulaklık");
                            wrongAnswers.add("Beyaz");
                            wrongAnswers.add("Deniz");
                            wrongAnswers.add("Sandalye");
                            wrongAnswers.add("Perde");
                            wrongAnswers.add("Kapı");
                            wrongAnswers.add("Ağaç");
                            wrongAnswers.add("ordu");
                            wrongAnswers.add("deneme");
                            wrongAnswers.add("uçak");
                            wrongAnswers.add("resim");
                            wrongAnswers.add("kale");

                            HashMap<String, Object> postData= new HashMap<>();
                            postData.put("email",email);
                            postData.put("English",EngWord);
                            postData.put("DownloadUrl",downloadUrl);
                            postData.put("Turkish",TrWord);
                            postData.put("Means",Means);
                            postData.put("id", (countDoc+1));
                            postData.put("wrongAnswers", wrongAnswers);

                            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(UploadScreenActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadScreenActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadScreenActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            })
            ;
        }else{

        }

    }

    public void goHome(View view) {
        Intent intent = new Intent(UploadScreenActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permissin need the gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }
        }else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }

    private void registerLauncher(){
        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode()==RESULT_OK){
                    Intent intentFromResult= o.getData();
                    if (intentFromResult!=null){
                        imageData=intentFromResult.getData();
                        binding.getFile.setImageURI(imageData);


//
//                           try {
//                               if (Build.VERSION.SDK_INT>=28){
//                                   ImageDecoder.Source source = ImageDecoder.createSource(UploadScreen.this.getContentResolver(),imageData);
//
//                                   selectedImage = ImageDecoder.decodeBitmap(source);
//                                   binding.getFile.setImageBitmap(selectedImage);
//                               }
//                               else{
//                                   selectedImage= MediaStore.Images.Media.getBitmap(UploadScreen.this.getContentResolver(),imageData );
//                                   binding.getFile.setImageBitmap(selectedImage);
//                               }
//
//                           }catch (Exception e){
//                                e.printStackTrace();
//                           }

                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {
                if (o){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else {
                    Toast.makeText(UploadScreenActivity.this, "Permission Needed ", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


}