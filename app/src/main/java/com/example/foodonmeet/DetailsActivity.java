package com.example.foodonmeet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodonmeet.home.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getName();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String postId;

    private TextView tvDate;
    private CircleImageView imgProfile;
    private TextView tvDesc;
    private TextView tvNbGuests;
    private TextView tvTime;
    private CircleImageView imgHostPicture;
    private TextView tvHostNameAge;
    private ImageView imgHostFlag;

    private ProgressBar pbLoading;
    private RelativeLayout rlDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvDate = findViewById(R.id.tvDate);
        tvDesc = findViewById(R.id.tvDesc);
        imgProfile = findViewById(R.id.imgProfile);
        tvNbGuests = findViewById(R.id.tvNbGuests);
        tvTime = findViewById(R.id.tvTime);
        imgHostPicture = findViewById(R.id.eventHostPicture);
        tvHostNameAge = findViewById(R.id.eventHostNameAge);
        imgHostFlag = findViewById(R.id.eventHostFlag);

        pbLoading = findViewById(R.id.pbLoading);
        rlDetails = findViewById(R.id.rlDetails);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rlDetails.setVisibility(RelativeLayout.GONE);
        pbLoading.setVisibility(ProgressBar.VISIBLE);
        getPostData();

        toolbar.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getPostData() {
        db.collection("posts").document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Event event = document.toObject(Event.class);
                                tvDate.setText(event.getDateString());
                                if(event.getDesc().isEmpty()) {
                                    tvDesc.setText(getResources().getText(R.string.no_desc));
                                    tvDesc.setTypeface(tvDesc.getTypeface(), Typeface.ITALIC);
                                }
                                else {
                                    tvDesc.setText(event.getDesc());
                                }
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                                        .child(event.getUser().getUID()+"_small");
                                final long SIZE = 300 * 300;
                                storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        imgProfile.setImageBitmap(bmp);
                                        imgHostPicture.setImageBitmap(bmp);
                                    }
                                });
                                int freeSpots = event.getNbGuestsMax() - event.getNbGuests();
                                tvNbGuests.setText(Integer.toString(freeSpots));
                                tvHostNameAge.setText(event.getUser().getName() + ", " + event.getUser().accessAge());
                                tvTime.setText(event.getTimeString());
                                imgHostFlag.setImageDrawable(getResources().getDrawable(event.getUser().accessFlag()));
                                pbLoading.setVisibility(ProgressBar.GONE);
                                rlDetails.setVisibility(RelativeLayout.VISIBLE);
                            } else {
                                Log.d("coucou", "No such document");
                            }
                        } else {
                            Log.d("coucou", "get failed with ", task.getException());
                        }
                    }
                });
    }
}
