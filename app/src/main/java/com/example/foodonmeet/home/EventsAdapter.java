package com.example.foodonmeet.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodonmeet.MainActivity;
import com.example.foodonmeet.R;
import com.example.foodonmeet.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LOCATION_SERVICE;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private ArrayList<Event> mList;
    final private OnListItemClickListener mOnListItemClickListener;
    private Context context;
    private FirebaseFirestore db;
    private final static String TAG = EventsAdapter.class.getName();

    public EventsAdapter(ArrayList<Event> list, OnListItemClickListener listener, Context context) {
        mList = list;
        mOnListItemClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_event, viewGroup, false);
        db = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventsAdapter.ViewHolder viewHolder, final int i) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(mList.get(i).getUser().getUID() + "_small");
        final long SIZE = 300 * 300;
        storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolder.hostPicture.setImageBitmap(bmp);
            }
        });
        viewHolder.title.setText(mList.get(i).getTitle());
        viewHolder.date.setText(mList.get(i).getDateString() + " at " + mList.get(i).getTimeString());
        viewHolder.nbGuests.setNumStars(mList.get(i).getNbGuestsMax());
        viewHolder.nbGuests.setRating(mList.get(i).getNbGuests());
        viewHolder.hostNameAge.setText(mList.get(i).getUser().getName() + ", " + mList.get(i).getUser().accessAge());
        viewHolder.hostFlag.setImageDrawable(context.getResources().getDrawable(mList.get(i).getUser().accessFlag()));
        viewHolder.tvDistance.setText(getDistance(mList.get(i).getLatitude(), mList.get(i).getLongitude()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView hostPicture;
        TextView hostNameAge;
        ImageView hostFlag;
        TextView title;
        TextView date;
        RatingBar nbGuests;
        TextView tvDistance;


        ViewHolder(View itemView) {
            super(itemView);
            hostPicture = itemView.findViewById(R.id.eventHostPicture);
            hostNameAge = itemView.findViewById(R.id.eventHostNameAge);
            hostFlag = itemView.findViewById(R.id.eventHostFlag);
            title = itemView.findViewById(R.id.eventTitle);
            date = itemView.findViewById(R.id.eventDate);
            nbGuests = itemView.findViewById(R.id.eventGuestNb);
            tvDistance = itemView.findViewById(R.id.tvDistance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }

    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public String getDistance(double latitude, double longitude) {
        Location eventLocation = new Location("point A");

        eventLocation.setLatitude(latitude);
        eventLocation.setLongitude(longitude);

        Location myLocation = getLastKnownLocation();

        float distanceFloat = myLocation.distanceTo(eventLocation)/1000;

        return String.format("%.1f%n", distanceFloat) + "km";
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
