package com.example.foodonmeet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodonmeet.home.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateActivity extends AppCompatActivity {

    static final String TAG = CreateActivity.class.getName();

    ImageView imgView;
    EditText etTitle;
    EditText etDesc;
    Button btnSend;
    EditText etDatePicker;
    EditText etNbGuestsMax;
    DatePickerDialog picker;
    Uri uri;

    private final Integer PICK_IMAGE_REQUEST=1;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        db = FirebaseFirestore.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar_create);
        setSupportActionBar(toolbar);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        btnSend = findViewById(R.id.btnSend);
        etNbGuestsMax = findViewById(R.id.etNbGuests);
        etDatePicker = findViewById(R.id.etDatePicker);
        etDatePicker.setInputType(InputType.TYPE_NULL);
        imgView = findViewById(R.id.imgView);

        Intent intent = getIntent();

        if(intent.getExtras() != null && intent.getStringExtra("origin").equals("Crop")) {
            byte[] bitmapData = intent.getByteArrayExtra("img");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            imgView.setImageBitmap(bitmap);
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
            i.putExtra("origin", "Create");
            startActivity(i);
        }
    }

    public void sendInfos(View view) {
        User user = new User("username");
        int userId = 1;

        String idPost = UUID.randomUUID().toString();
        Post post = new Post(etTitle.getText().toString(), userId, etDatePicker.getText().toString(),
                12, 0,Integer.parseInt(etNbGuestsMax.getText().toString()), idPost);

        db.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void openDatePicker(View view){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(CreateActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH);
                        Date date = new Date(selectedYear, selectedMonth, selectedDay-1);
                        String dayOfWeek = simpledateformat.format(date);
                        etDatePicker.setText(dayOfWeek);
                    }
                }, year, month, day);
        picker.show();
    }

    public void cancelCreate(View view) {
        Toast.makeText(this, "New post canceled.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
