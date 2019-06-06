package com.example.foodonmeet;

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
import com.example.foodonmeet.home.EventsAdapter;
import com.example.foodonmeet.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodonmeet.ChatActivity.TAG;


public class ChatFragment extends Fragment {

    ChatRoomRepository chatRoomRepository;
    private RecyclerView chatRooms;
    private ChatRoomsAdapter adapter;
    private ProgressBar pbLoading;
    private ArrayList<ChatRoom> rv_list;

    FirebaseFirestore db;

    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRoomRepository = new ChatRoomRepository(FirebaseFirestore.getInstance());

        db = FirebaseFirestore.getInstance();

        chatRooms = (RecyclerView) view.findViewById(R.id.rvChats);
        /*chatRooms.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        pbLoading = view.findViewById(R.id.pbLoading);

        rv_list = new ArrayList<ChatRoom>();

        //getChatRooms();

        db.collection("rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ChatRoom chatRoom = document.toObject(ChatRoom.class);
                                    rv_list.add(chatRoom);
                                    ChatRoomsAdapter mAdapter = new ChatRoomsAdapter(rv_list,listener);
                                    chatRooms.setAdapter(mAdapter);
                                    chatRooms.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }
                            pbLoading.setVisibility(ProgressBar.GONE);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return view;
    }

    private void getChatRooms() {
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
    }

    ChatRoomsAdapter.OnChatRoomClickListener listener = new ChatRoomsAdapter.OnChatRoomClickListener() {
        @Override
        public void onClick(ChatRoom chatRoom) {
            Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
            intent.putExtra(ChatRoomActivity.CHAT_ROOM_ID, chatRoom.getName());
            intent.putExtra(ChatRoomActivity.USERNAME, chatRoom.getName());
            startActivity(intent);
        }
    };
}
