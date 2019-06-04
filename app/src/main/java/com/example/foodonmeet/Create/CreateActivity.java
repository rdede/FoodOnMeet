package com.example.foodonmeet.Create;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodonmeet.CropImageActivity;
import com.example.foodonmeet.MainActivity;
import com.example.foodonmeet.ProfileSetup.ProfileSetupBirthdateFragment;
import com.example.foodonmeet.ProfileSetup.ProfileSetupEmailFragment;
import com.example.foodonmeet.ProfileSetup.ProfileSetupNameFragment;
import com.example.foodonmeet.ProfileSetup.ProfileSetupNationalityFragment;
import com.example.foodonmeet.ProfileSetup.ProfileSetupPasswordFragment;
import com.example.foodonmeet.R;
import com.example.foodonmeet.User;
import com.example.foodonmeet.home.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CreateActivity extends AppCompatActivity {

    static final String TAG = CreateActivity.class.getName();

    private Fragment fragmentDate = new CreateDateFragment();
    private Fragment fragmentTime = new CreateTimeFragment();
    private Fragment fragmentLocation = new CreateLocationFragment();
    private Fragment fragmentNbGuests = new CreateNbGuestsFragment();
    private Fragment fragmentTitle = new CreateTitleFragment();
    private Fragment fragmentDesc = new CreateDescFragment();
    private Fragment active = fragmentDate;

    private FragmentManager fm = getSupportFragmentManager();

    private Date date;
    private int nbGuests;
    private String title;
    private String desc;
    private double latitude;
    private double longitude;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Create new event");

        fm.beginTransaction().add(R.id.main_container,fragmentDesc, "desc").hide(fragmentDesc).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentTitle, "title").hide(fragmentTitle).commit();
        fm.beginTransaction().add(R.id.main_container,fragmentNbGuests, "nbGuests").hide(fragmentNbGuests).commit();
        fm.beginTransaction().add(R.id.main_container,fragmentLocation, "location").hide(fragmentLocation).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentTime, "time").hide(fragmentTime).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentDate, "date").commit();

        toolbar.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLastFragment();
            }
        });
    }

    @Override
    public void onBackPressed(){
        goToLastFragment();
    }

    private void goToLastFragment(){
        switch(active.getTag()) {
            case "date": super.onBackPressed();
                break;
            case "time": fm.beginTransaction().hide(fragmentTime).show(fragmentDate).commit();
                active = fragmentDate;
                break;
            case "location": fm.beginTransaction().hide(fragmentLocation).show(fragmentTime).commit();
                active = fragmentTime;
                break;
            case "nbGuests": fm.beginTransaction().hide(fragmentNbGuests).show(fragmentLocation).commit();
                active = fragmentLocation;
                break;
            case "title": fm.beginTransaction().hide(fragmentTitle).show(fragmentNbGuests).commit();
                active = fragmentNbGuests;
                break;
            case "desc": fm.beginTransaction().hide(fragmentDesc).show(fragmentTitle).commit();
                active = fragmentTitle;
                break;
        }
    }

    public void goToNext(View view) {
        switch(active.getTag()) {

            case "date": /*fm.beginTransaction().hide(fragmentDate).show(fragmentTime).commit();
                active = fragmentTime;*/
                break;


            case "time": CreateTimeFragment fragment1 = (CreateTimeFragment)fm.findFragmentByTag("time");
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, fragment1.getHours());
                cal.set(Calendar.MINUTE, fragment1.getMinutes());
                date = cal.getTime();
                Log.d("coucou", date.toString());
                fm.beginTransaction().hide(fragmentTime).show(fragmentLocation).commit();
                active = fragmentLocation;
                break;


            case "location": CreateLocationFragment fragment2 = (CreateLocationFragment)fm.findFragmentByTag("location");
                LatLng latLng = fragment2.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                Log.d("coucou", "lat : " + latitude + ", lng : " + longitude);
                /*Geocoder geocoder = new Geocoder(this);
                List<Address> addresses = new ArrayList<>();
                try {
                    Log.d("coucou", "address" + fragment2.getAddress());
                    addresses = geocoder.getFromLocationName(fragment2.getAddress(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses.size() > 0) {
                    latitude= addresses.get(0).getLatitude();
                    longitude= addresses.get(0).getLongitude();
                }*/
                fm.beginTransaction().hide(fragmentLocation).show(fragmentNbGuests).commit();
                active = fragmentNbGuests;
                break;


            case "nbGuests": CreateNbGuestsFragment fragment3 = (CreateNbGuestsFragment) fm.findFragmentByTag("nbGuests");
                nbGuests = fragment3.getNbGuests();
                fm.beginTransaction().hide(fragmentNbGuests).show(fragmentTitle).commit();
                active = fragmentTitle;
                break;


            case "title": CreateTitleFragment fragment4 = (CreateTitleFragment) fm.findFragmentByTag("title");
                title = fragment4.getTitle();
                fm.beginTransaction().hide(fragmentTitle).show(fragmentDesc).commit();
                active = fragmentDesc;
                break;


            case "desc": CreateDescFragment fragment5 = (CreateDescFragment) fm.findFragmentByTag("desc");
                desc = fragment5.getDesc();
                sendInfos();
                finish();
                break;
        }
    }

    public void sendInfos() {
        db.collection("users").document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        User user = document.toObject(User.class);
                        Event post = new Event(title, user, date, desc, 0, nbGuests, null, new ArrayList<String>(), latitude, longitude);
                        db.collection("posts")
                                .add(post)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        documentReference.update("postId", documentReference.getId());
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CreateActivity.this, "Picture download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDate(Date dateFragment) {
        date = dateFragment;
        fm.beginTransaction().hide(fragmentDate).show(fragmentTime).commit();
        active = fragmentTime;
    }
}
