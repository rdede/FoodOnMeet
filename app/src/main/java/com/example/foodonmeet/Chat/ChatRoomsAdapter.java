package com.example.foodonmeet.Chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodonmeet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomViewHolder> {

    private List<ChatRoom> chatRooms;
    private OnListItemClickListener listener;

    /*public interface OnChatRoomClickListener {
        void onClick(ChatRoom chatRoom);
    }*/

    public ChatRoomsAdapter(List<ChatRoom> chatRooms, OnListItemClickListener listener) {
        this.chatRooms = chatRooms;
        this.listener = listener;
    }

    //private OnChatRoomClickListener listener;

        @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatRoomViewHolder holder, int position) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(chatRooms.get(position).getUid() + "_small");
        final long SIZE = 600 * 600;
        storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imgProfile.setImageBitmap(bmp);
            }
        });
        holder.name.setText(chatRooms.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ChatRoom chatRoom;
        CircleImageView imgProfile;

        public ChatRoomViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_room_name);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onListItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}