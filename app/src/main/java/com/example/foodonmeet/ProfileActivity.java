package com.example.foodonmeet;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Uri uri;

    CircleImageView imgProfile;

    private final Integer PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        imgProfile = findViewById(R.id.profilePicture);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Profile");

        toolbar.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        loadImageFromStorage(directory.getAbsolutePath());
    }

    public void signOut(View view) {
        mAuth.signOut();
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, mAuth.getCurrentUser().getUid()+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public void chooseImg(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            Intent i = new Intent(getApplicationContext(), CropImageActivity.class);
            i.setData(uri);
            i.putExtra("origin", "Profile");
            startActivity(i);
        }
    }

    public void goToSettings(View view) {
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
    }

    public void goToEditProfile(View view) {
        Intent i = new Intent(getApplicationContext(), ProfileEditActivity.class);
        startActivity(i);
    }
}
