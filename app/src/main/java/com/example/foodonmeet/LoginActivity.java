package com.example.foodonmeet;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodonmeet.ProfileSetup.ProfileSetupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    private FirebaseAuth mAuth;

    EditText etEmail;
    EditText etPassword;
    TextView tvSignup;

    LinearLayout llLogin;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        FirebaseApp.initializeApp(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);

        llLogin = findViewById(R.id.llLogin);
        pbLoading = findViewById(R.id.pbLoading);

        String signupString = getString(R.string.no_account);
        SpannableString signupSs = new SpannableString(signupString);

        ClickableSpan signupCs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i = new Intent(getApplicationContext(), ProfileSetupActivity.class);
                startActivity(i);
            }
        };

        signupSs.setSpan(signupCs, 17, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignup.setText(signupSs);
        tvSignup.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void signIn(View view) {
        setPbVisible();
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Authentication succeed.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void setPbVisible() {
        llLogin.setVisibility(LinearLayout.GONE);
        pbLoading.setVisibility(ProgressBar.VISIBLE);
    }
}
