package com.example.foodonmeet.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment implements EventsAdapter.OnListItemClickListener {

    private final static String TAG = HomeFragment.class.getName();

    private ArrayList<Event> rv_list;
    private RecyclerView recyclerView;

    private ProgressBar pbLoading;

    FirebaseFirestore db;
    FirebaseAuth mAuth;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        pbLoading = view.findViewById(R.id.pbLoading);

        rv_list = new ArrayList<Event>();

        /*Date c = Calendar.getInstance().getTime();
        User user = new User("username");*/


        Location myLocation = getLastKnownLocation();
        Log.d("coucou", String.valueOf(myLocation.getLatitude()));

        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.distance_preferences), getActivity().MODE_PRIVATE);
        final int distanceValue = prefs.getInt("dist", 30);


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
                                        && !event.getListGuests().contains(mAuth.getUid())
                                        && !event.getUser().getUID().equals(mAuth.getUid())
                                        && getDistance(event.getLatitude(), event.getLongitude()) < distanceValue) {
                                    rv_list.add(event);
                                }
                            }
                            Collections.sort(rv_list, new Comparator<Event>(){
                                @Override
                                public int compare(Event o1, Event o2) {
                                    return Float.compare(getDistance(o1.getLatitude(), o1.getLongitude()), getDistance(o2.getLatitude(), o2.getLongitude()));
                                }
                            });
                            EventsAdapter mAdapter = new EventsAdapter(rv_list, HomeFragment.this, getContext());
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        i.putExtra("origin", "home");
        startActivity(i);
    }

    public float getDistance(double latitude, double longitude) {
        Location eventLocation = new Location("point A");

        eventLocation.setLatitude(latitude);
        eventLocation.setLongitude(longitude);

        Location myLocation = getLastKnownLocation();

        float distanceFloat = myLocation.distanceTo(eventLocation)/1000;

        return distanceFloat;
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
