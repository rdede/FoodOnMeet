package com.example.foodonmeet.ProfileSetup;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.foodonmeet.LoginActivity;
import com.example.foodonmeet.MainActivity;
import com.example.foodonmeet.R;
import com.example.foodonmeet.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class ProfileSetupActivity extends AppCompatActivity {

    private static final String TAG = ProfileSetupActivity.class.getName();

    private Fragment fragmentName = new ProfileSetupNameFragment();
    private Fragment fragmentBirthdate = new ProfileSetupBirthdateFragment();
    private Fragment fragmentNationality = new ProfileSetupNationalityFragment();
    private Fragment fragmentEmail = new ProfileSetupEmailFragment();
    private Fragment fragmentPassword = new ProfileSetupPasswordFragment();
    private Fragment active = fragmentName;

    private String name;
    private Date birthdate;
    private String nationality;
    private String email;
    private String password;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    Toolbar toolbar;
    TextView toolbarTitle;

    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Name");

        ImageButton btnClose = toolbar.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLastFragment();
            }
        });

        fm.beginTransaction().add(R.id.main_container,fragmentPassword, "password").hide(fragmentPassword).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentEmail, "email").hide(fragmentEmail).commit();
        fm.beginTransaction().add(R.id.main_container,fragmentNationality, "nationality").hide(fragmentNationality).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentBirthdate, "birthdate").hide(fragmentBirthdate).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentName, "name").commit();
    }

    @Override
    public void onBackPressed(){
        goToLastFragment();
    }

    private void goToLastFragment(){
        switch(active.getTag()) {
            case "name": super.onBackPressed();
                break;
            case "birthdate": fm.beginTransaction().hide(fragmentBirthdate).show(fragmentName).commit();
                active = fragmentName;
                break;
            case "nationality": fm.beginTransaction().hide(fragmentNationality).show(fragmentBirthdate).commit();
                active = fragmentBirthdate;
                break;
            case "email": fm.beginTransaction().hide(fragmentEmail).show(fragmentNationality).commit();
                active = fragmentNationality;
                break;
            case "password": fm.beginTransaction().hide(fragmentPassword).show(fragmentEmail).commit();
                active = fragmentEmail;
                break;
        }
    }

    public void goToBirthdate(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if(focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(), 0);
        }
        ProfileSetupNameFragment fragment = (ProfileSetupNameFragment)fm.findFragmentByTag("name");
        toolbarTitle.setText("Birthdate");
        active = fragmentBirthdate;
        name = fragment.getName();
        fm.beginTransaction().hide(fragmentName).show(fragmentBirthdate).commit();
    }

    public void goToNationality(View view) {
        ProfileSetupBirthdateFragment fragment = (ProfileSetupBirthdateFragment)fm.findFragmentByTag("birthdate");
        toolbarTitle.setText("Nationality");
        active = fragmentNationality;
        birthdate = fragment.getBirthdate();
        fm.beginTransaction().hide(fragmentBirthdate).show(fragmentNationality).commit();
    }

    public void goToEmail(View view) {
        ProfileSetupNationalityFragment fragment = (ProfileSetupNationalityFragment)fm.findFragmentByTag("nationality");
        toolbarTitle.setText("Email");
        active = fragmentEmail;
        nationality = fragment.getNationality();
        fm.beginTransaction().hide(fragmentNationality).show(fragmentEmail).commit();
    }

    public void goToPassword(View view) {
        ProfileSetupEmailFragment fragment = (ProfileSetupEmailFragment)fm.findFragmentByTag("email");
        toolbarTitle.setText("Password");
        active = fragmentPassword;
        email = fragment.getEmail();
        fm.beginTransaction().hide(fragmentEmail).show(fragmentPassword).commit();
    }

    public void goToApp(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(), 0);
        ProfileSetupPasswordFragment fragment = (ProfileSetupPasswordFragment)fm.findFragmentByTag("password");
        password = fragment.getPassword();
        fragment.setPbVisible();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(mAuth.getUid(), name, birthdate, nationality);
                            db.collection("users").document(mAuth.getCurrentUser().getUid())
                                    .set(user)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.getMessage());
                                        }
                                    });
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Toast.makeText(ProfileSetupActivity.this, "Authentication succeed.",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(i);
                                            } else {
                                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                Toast.makeText(ProfileSetupActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        } else {
                            Log.d("coucou", "error while creating the account");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSetupActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
