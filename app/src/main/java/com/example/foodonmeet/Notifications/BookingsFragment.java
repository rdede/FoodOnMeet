package com.example.foodonmeet.Notifications;

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

import com.example.foodonmeet.R;
import com.example.foodonmeet.home.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class BookingsFragment extends Fragment implements BookingsAdapter.OnListItemClickListener {

    private static final String TAG = BookingsFragment.class.getName();

    private ArrayList<Booking> rv_list;
    private RecyclerView recyclerView;
    private BookingsAdapter mAdapter;

    private ProgressBar pbLoading;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public BookingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //pbLoading = view.findViewById(R.id.pbLoading);

        rv_list = new ArrayList<Booking>();

        //pbLoading.setVisibility(ProgressBar.VISIBLE);

        db.collection("bookings").whereEqualTo("receiverUid", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Booking booking = document.toObject(Booking.class);
                                rv_list.add(booking);
                                mAdapter = new BookingsAdapter(rv_list, BookingsFragment.this,getContext());
                                recyclerView.setAdapter(mAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }
                            //pbLoading.setVisibility(ProgressBar.GONE);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return view;
    }

    @Override
    public void onAcceptClick(int clickerItemIndex) {
        final DocumentReference ref = db.collection("posts").document(rv_list.get(clickerItemIndex).getPostId());

        ref.update("listGuests", FieldValue.arrayUnion(rv_list.get(clickerItemIndex).getRequesterUid()));
        ref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            Event event = task.getResult().toObject(Event.class);
                            int nbGuests = event.getNbGuests()+1;
                            Log.d("coucou", Integer.toString(nbGuests));
                            ref.update("nbGuests", nbGuests);
                        }
                    }
                });
        db.collection("bookings").whereEqualTo("bookingId", rv_list.get(clickerItemIndex).getBookingId())
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()) {
                                db.collection("bookings").document(document.getId()).delete();
                            }
                        }
                    }
                });
        rv_list.remove(clickerItemIndex);
        mAdapter.notifyItemRemoved(clickerItemIndex);
    }

    @Override
    public void onDeclineClick(int clickerItemIndex) {
        db.collection("bookings").whereEqualTo("bookingId", rv_list.get(clickerItemIndex).getBookingId())
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()) {
                                db.collection("bookings").document(document.getId()).delete();
                            }
                        }
                    }
                });
        rv_list.remove(clickerItemIndex);
        mAdapter.notifyItemRemoved(clickerItemIndex);
    }
}
