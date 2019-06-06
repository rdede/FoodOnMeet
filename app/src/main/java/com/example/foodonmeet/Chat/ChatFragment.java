package com.example.foodonmeet.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.foodonmeet.Chat.ChatRoom;
import com.example.foodonmeet.Chat.ChatRoomRepository;
import com.example.foodonmeet.Chat.ChatRoomsAdapter;
import com.example.foodonmeet.ChatRoomActivity;
import com.example.foodonmeet.R;
import com.example.foodonmeet.User;
import com.example.foodonmeet.home.EventsAdapter;
import com.example.foodonmeet.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements ChatRoomsAdapter.OnListItemClickListener{

    private static final String TAG = ChatFragment.class.getName();

    ChatRoomRepository chatRoomRepository;
    private RecyclerView chatRooms;
    private ChatRoomsAdapter adapter;
    private ProgressBar pbLoading;
    private ArrayList<ChatRoom> rv_list;

    private String uid2;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRoomRepository = new ChatRoomRepository(FirebaseFirestore.getInstance());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        chatRooms = (RecyclerView) view.findViewById(R.id.rvChats);
        /*chatRooms.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        pbLoading = view.findViewById(R.id.pbLoading);

        rv_list = new ArrayList<ChatRoom>();

        //getChatRooms();


        db.collection("rooms").whereArrayContains("uids", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> uids = new ArrayList<>();
                                uids = (ArrayList<String>) document.get("uids");
                                for (String uid : uids) {
                                    if(!uid.equals(mAuth.getUid()))
                                        uid2 = uid;
                                }
                                db.collection("users").document(uid2)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    User user = task.getResult().toObject(User.class);
                                                    ChatRoom chatRoom = new ChatRoom(user.getUID(), user.getName());
                                                    rv_list.add(chatRoom);
                                                }
                                            }
                                        });
                            }
                            ChatRoomsAdapter mAdapter = new ChatRoomsAdapter(rv_list,ChatFragment.this);
                            chatRooms.setAdapter(mAdapter);
                            chatRooms.setLayoutManager(new LinearLayoutManager(getActivity()));
                            pbLoading.setVisibility(ProgressBar.GONE);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return view;
    }

    /*private void getChatRooms() {
        chatRoomRepository.getRooms(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("MainActivity", "Listen failed.", e);
                    return;
                }

                List<ChatRoom> rooms = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    rooms.add(new ChatRoom(doc.get("uid1").toString(), doc.get("uid2").toString(), doc.getString("name")));
                }

                adapter = new ChatRoomsAdapter(rooms, listener);
                chatRooms.setAdapter(adapter);
            }
        });
    }*/

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra(ChatRoomActivity.CHAT_ROOM_ID, rv_list.get(clickedItemIndex).getName());
        intent.putExtra(ChatRoomActivity.USERNAME, rv_list.get(clickedItemIndex).getName());
        startActivity(intent);
    }
}
