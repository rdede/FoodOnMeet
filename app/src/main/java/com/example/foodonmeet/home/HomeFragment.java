package com.example.foodonmeet.home;

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

import com.example.foodonmeet.DetailsActivity;
import com.example.foodonmeet.R;
import com.example.foodonmeet.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class HomeFragment extends Fragment implements EventsAdapter.OnListItemClickListener {

    private final static String TAG = HomeFragment.class.getName();

    private ArrayList<Event> rv_list;
    private RecyclerView recyclerView;

    private ProgressBar pbLoading;

    FirebaseFirestore db;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        db = FirebaseFirestore.getInstance();

        pbLoading = view.findViewById(R.id.pbLoading);

        rv_list = new ArrayList<Event>();

        /*Date c = Calendar.getInstance().getTime();
        User user = new User("username");*/

        pbLoading.setVisibility(ProgressBar.VISIBLE);

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                if(event.getDate().after(Calendar.getInstance().getTime())) {
                                    rv_list.add(event);
                                    EventsAdapter mAdapter = new EventsAdapter(rv_list, HomeFragment.this, getContext());
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
        startActivity(i);
    }
}
