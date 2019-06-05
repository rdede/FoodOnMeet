package com.example.foodonmeet.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodonmeet.R;

import java.util.List;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomViewHolder> {

    private List<ChatRoom> chatRooms;

    public ChatRoomsAdapter(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_chat_room,
                parent,
                false
        );
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatRoomViewHolder holder, int position) {
        holder.bind(chatRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ChatRoom chatRoom;

        public ChatRoomViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_room_name);
        }

        public void bind(ChatRoom chatRoom) {
            this.chatRoom = chatRoom;
            name.setText(chatRoom.getName());
        }
    }
}