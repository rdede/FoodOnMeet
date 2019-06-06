package com.example.foodonmeet;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.foodonmeet.Create.CreateActivity;
import com.example.foodonmeet.Notifications.BookingsFragment;
import com.example.foodonmeet.Notifications.NotificationsFragment;
import com.example.foodonmeet.home.HomeFragment;
import com.example.foodonmeet.home.EventsAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements EventsAdapter.OnListItemClickListener {

    static final String TAG = MainActivity.class.getName();

    CircleImageView imgProfile;

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new ChatFragment();
    final Fragment fragment3 = new NotificationsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    FirebaseAuth mAuth;

    private TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        imgProfile = findViewById(R.id.imgProfile);

        mAuth = FirebaseAuth.getInstance();

        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);

        toolbar.findViewById(R.id.imgProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(settingsIntent);
            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_small");

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        loadImageFromStorage(directory.getAbsolutePath());
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent i = new Intent(getApplicationContext(), ChatsAdapter.ProfileActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){ }

    public void goToCreate(View view) {
        Intent createIntent = new Intent(getApplicationContext(), CreateActivity.class);
        startActivity(createIntent);
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f = new File(path, mAuth.getCurrentUser().getUid()+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    toolbarTitle.setText("Home");
                    active = fragment1;
                    return true;

                case R.id.navigation_chat:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    toolbarTitle.setText("Chat");
                    active = fragment2;
                    return true;

                case R.id.navigation_notifications:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    toolbarTitle.setText("Notifications");
                    active = fragment3;
                    return true;
            }
            return false;
        }
    };
}
