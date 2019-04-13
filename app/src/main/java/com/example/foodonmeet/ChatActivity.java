package com.example.foodonmeet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements ChatsAdapter.OnChatsItemClickListener {

    RecyclerView mList;
    RecyclerView.Adapter mListAdapter;

    FirebaseFirestore db;

    Bitmap bitmap;

    static final String TAG = ChatActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = FirebaseFirestore.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_chat:

                                break;
                            case R.id.navigation_home:
                                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(homeIntent);
                                break;
                            case R.id.navigation_notifications:
                                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(loginIntent);
                                break;
                        }
                        return false;
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.findViewById(R.id.btnAdd).setVisibility(View.GONE);
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText("Chat");

        mList = findViewById(R.id.rvChats);
        mList.hasFixedSize();
        mList.setLayoutManager(new LinearLayoutManager(this));

        CollectionReference users = db.collection("users");

        users.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final UserChat currentUser = document.toObject(UserChat.class);

                                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                                        .child(currentUser.getUid()+"_small");

                                final long SIZE = 300 * 300;
                                storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        loadData(currentUser.getName(), bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(ChatActivity.this, "Picture download failed", Toast.LENGTH_SHORT).show();
                                        loadData(currentUser.getName(), null);
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        /*StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_small");



        final long SIZE = 300 * 300;
        storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                loadData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ChatActivity.this, "Picture download failed", Toast.LENGTH_SHORT).show();
            }
        });*/


        /*FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                items.add(document.toObject(UserChat.class));
                                mListAdapter = new ChatsAdapter(items, ChatActivity.this);
                                mList.setAdapter(mListAdapter);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/
    }

    @Override
    public void onChatsItemClick(int clickedItemIndex) {

    }

    public void loadData(String name, Bitmap bitmap) {
        final ArrayList<UserChat> items = new ArrayList<>();
        items.add(new UserChat(name, bitmap));

        mListAdapter = new ChatsAdapter(items, ChatActivity.this);
        mList.setAdapter(mListAdapter);
    }
}
