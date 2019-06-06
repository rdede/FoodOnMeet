package com.example.foodonmeet.Notifications;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.foodonmeet.DetailsActivity;
import com.example.foodonmeet.R;
import com.example.foodonmeet.home.Event;
import com.example.foodonmeet.home.EventsAdapter;
import com.example.foodonmeet.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class MyEventsFragments extends Fragment implements EventsAdapter.OnListItemClickListener {

    private static final String TAG = MyEventsFragments.class.getName();

    private ArrayList<Event> rv_list;
    private RecyclerView recyclerView;

    private ProgressBar pbLoading;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public MyEventsFragments() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        pbLoading = view.findViewById(R.id.pbLoading);

        rv_list = new ArrayList<Event>();

        pbLoading.setVisibility(ProgressBar.VISIBLE);

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                if (event.getDate().after(Calendar.getInstance().getTime())
                                        && event.getListGuests().contains(mAuth.getUid())
                                        && !event.getUser().getUID().equals(mAuth.getUid())) {
                                    rv_list.add(event);
                                    EventsAdapter mAdapter = new EventsAdapter(rv_list, MyEventsFragments.this, getContext());
                                    recyclerView.setAdapter(mAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                }
                            }
                            pbLoading.setVisibility(ProgressBar.GONE);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent i = new Intent(getActivity(), DetailsActivity.class);
        i.putExtra("postId", rv_list.get(clickedItemIndex).getPostId());
        i.putExtra("postName", rv_list.get(clickedItemIndex).getTitle());
        i.putExtra("origin", "myEvents");
        startActivity(i);
    }
}
