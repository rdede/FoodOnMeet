package com.example.foodonmeet;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    private Uri uri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private CircleImageView imgProfile;
    private EditText etBio;

    private int distanceValue;

    private TextView tvDistance;
    private SeekBar sbDistance;

    private final Integer PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        imgProfile = findViewById(R.id.profilePicture);
        etBio = findViewById(R.id.etBio);


        tvDistance = findViewById(R.id.tvDistanceDisplay);
        sbDistance = findViewById(R.id.sbDistance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = this;

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Edit Profile");

        SharedPreferences prefs = getSharedPreferences(getString(R.string.distance_preferences), MODE_PRIVATE);
        int value = prefs.getInt("dist", 30);

        tvDistance.setText(value + "km");
        sbDistance.setProgress(value);


        toolbar.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(mAuth.getUid())
                        .update("bio", etBio.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.distance_preferences), MODE_PRIVATE).edit();
                                editor.putInt("dist",distanceValue);
                                editor.apply();
                                finish();
                            }
                        });
            }
        });

        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDistance.setText(progress + "km");
                distanceValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        loadImageFromStorage(directory.getAbsolutePath());
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
}
