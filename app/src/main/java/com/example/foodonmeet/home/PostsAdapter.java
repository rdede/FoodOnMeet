package com.example.foodonmeet.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodonmeet.R;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private ArrayList<Post> mList;
    final private OnListItemClickListener mOnListItemClickListener;

    public PostsAdapter(ArrayList<Post> list, OnListItemClickListener listener) {
        mList = list;
        mOnListItemClickListener = listener;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.img.setImageBitmap(mList.get(i).getBitmap());
        viewHolder.title.setText(mList.get(i).getTitle());
        viewHolder.date.setText(mList.get(i).getDate().toString());
        viewHolder.nbGuests.setText(Integer.toString(mList.get(i).getNbGuests())+"/"
                +Integer.toString(mList.get(i).getNbGuestsMax()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img;
        TextView title;
        TextView date;
        TextView nbGuests;


        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.listItemImg);
            title = itemView.findViewById(R.id.listItemTitle);
            date = itemView.findViewById(R.id.listItemDate);
            nbGuests = itemView.findViewById(R.id.listItemNbGuests);

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


}
