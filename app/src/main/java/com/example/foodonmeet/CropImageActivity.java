package com.example.foodonmeet;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodonmeet.Create.CreateActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CropImageActivity extends AppCompatActivity {

    ImageCropView imageCropView;
    ProgressBar pbLoading;

    String originActivity;

    Bitmap bitmap;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        Toolbar toolbar = findViewById(R.id.crop_toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        imageCropView = findViewById(R.id.imageCropView);
        pbLoading = findViewById(R.id.pbLoading);

        imageCropView.setGridInnerMode(ImageCropView.GRID_ON);
        imageCropView.setGridOuterMode(ImageCropView.GRID_ON);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        originActivity = intent.getStringExtra("origin");

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            imageCropView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelCrop(View view) {
        Toast.makeText(this, "Upload canceled.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void sendImg(View view) {
        pbLoading.setVisibility(ProgressBar.VISIBLE);
        bitmap = imageCropView.getCroppedImage();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] dataLarge = bytes.toByteArray();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        UploadTask uploadTaskLarge = ref.putBytes(dataLarge);
        uploadTaskLarge.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CropImageActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadSmall();
            }
        });
        saveToInternalStorage(bitmap);
    }

    public void uploadSmall() {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "_small");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap small = bitmap.createScaledBitmap(bitmap, 300, 300, false);
        small.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] dataSmall = bytes.toByteArray();

        UploadTask uploadTaskSmall = ref.putBytes(dataSmall);
        uploadTaskSmall.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CropImageActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CropImageActivity.this, "Profile picture successfully uploaded.", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        saveToInternalStorage(small);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        File mypath = new File(directory, mAuth.getCurrentUser().getUid() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}
