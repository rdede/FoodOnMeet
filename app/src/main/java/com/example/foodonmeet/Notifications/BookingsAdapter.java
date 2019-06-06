package com.example.foodonmeet.Notifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodonmeet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder> {

    private ArrayList<Booking> mList;
    final private OnListItemClickListener mOnListItemClickListener;
    private Context context;

    public BookingsAdapter(ArrayList<Booking> mList, OnListItemClickListener mOnListItemClickListener, Context context) {
        this.mList = mList;
        this.mOnListItemClickListener = mOnListItemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_booking, viewGroup, false);
        return new BookingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(mList.get(i).getRequesterUid());
        final long SIZE = 300 * 300;
        storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolder.imgProfile.setImageBitmap(bmp);
            }
        });
        viewHolder.tvTitle.setText(mList.get(i).getPostName());
        viewHolder.tvNameAge.setText(mList.get(i).getName());
        viewHolder.imgFlag.setImageDrawable(context.getResources().getDrawable(mList.get(i).getFlag()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgProfile;
        TextView tvNameAge;
        ImageView imgFlag;
        TextView tvTitle;
        ImageButton btnAccept;
        ImageButton btnDecline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvNameAge = itemView.findViewById(R.id.tvNameAge);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            tvTitle = itemView.findViewById(R.id.tvPostTitle);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnListItemClickListener.onAcceptClick(getAdapterPosition());
                }
            });

            btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnListItemClickListener.onDeclineClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnListItemClickListener {
        void onAcceptClick(int clickerItemIndex);

        void onDeclineClick(int clickerItemIndex);
    }
}
