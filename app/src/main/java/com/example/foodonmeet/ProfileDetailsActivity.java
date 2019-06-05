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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodonmeet.Chat.ChatRoomRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends AppCompatActivity {

    private final static String TAG = ProfileDetailsActivity.class.getName();

    private FirebaseFirestore db;

    private CircleImageView imgProfile;
    private TextView tvName;
    private TextView tvAge;
    private TextView tvNationality;
    private ImageView ivFlag;
    private TextView tvDesc;
    private Button btnContact;
    private ProgressBar pbLoading;
    private RelativeLayout rlProfileDetails;

    private TextView toolbarTitle;

    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        db = FirebaseFirestore.getInstance();

        imgProfile = findViewById(R.id.imgProfile);
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvNationality = findViewById(R.id.tvNationality);
        ivFlag = findViewById(R.id.ivFlag);
        tvDesc = findViewById(R.id.tvDesc);
        btnContact = findViewById(R.id.btnSendMessage);
        pbLoading = findViewById(R.id.pbLoading);
        rlProfileDetails = findViewById(R.id.rlProfileDetails);

        Intent intent = getIntent();
        profileId = intent.getStringExtra("uid");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);


        toolbar.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlProfileDetails.setVisibility(RelativeLayout.GONE);
        pbLoading.setVisibility(ProgressBar.VISIBLE);
        db.collection("users").document(profileId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                tvName.setText(user.getName());
                                toolbarTitle.setText(user.getName());
                                tvAge.setText(user.accessAge() + " years old");
                                tvNationality.setText(user.getNationality());
                                ivFlag.setImageDrawable(getResources().getDrawable(user.accessFlag()));
                                if (!user.getBio().isEmpty()) {
                                    tvDesc.setText(user.getBio());
                                } else {
                                    tvDesc.setText("No description available");
                                    tvDesc.setTypeface(null, Typeface.ITALIC);
                                    tvDesc.setTextColor(getResources().getColor(R.color.grey_500));
                                }
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                                        .child(user.getUID() + "_small");
                                final long SIZE = 300 * 300;
                                storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        imgProfile.setImageBitmap(bmp);
                                    }
                                });
                                rlProfileDetails.setVisibility(RelativeLayout.VISIBLE);
                                pbLoading.setVisibility(ProgressBar.GONE);
                            }
                            else {
                                Log.d("coucou", "document doesn't exist");
                            }
                        }
                        else {
                            Log.d("coucou", "error while getting the document");
                        }
                    }
                });
        btnContact = findViewById(R.id.btnSendMessage);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });
    }

    public void sendMessage(View view) {
        /*Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("profileUid", profileId);
        startActivity(i);*/
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (profileId.length() > uid.length()){
            String roomName = profileId.concat(uid);
            String uid1 = profileId;
            String uid2 = uid;
            createRoom(roomName, uid1, uid2);
        } else {
            String roomName = uid.concat(profileId);
            String uid1 = uid;
            String uid2 = profileId;
            createRoom(roomName, uid1, uid2);
        }
    }

    private void createRoom(String roomName, String uid1, String uid2) {
        ChatRoomRepository chatRoomRepository = new ChatRoomRepository(db);
        chatRoomRepository.createRoom(
                roomName,
                uid1,
                uid2,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(ProfileDetailsActivity.this, ChatRoomActivity.class);
                        intent.putExtra(ChatRoomActivity.CHAT_ROOM_ID, documentReference.getId());
                        intent.putExtra(ChatRoomActivity.USERNAME, tvName.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                ProfileDetailsActivity.this,
                                "Error creating chat room",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}
