package com.example.foodonmeet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodonmeet.Notifications.BookingsFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private ArrayList<User> mChats;
    final private OnChatsItemClickListener mOnChatsItemClickListener;

    ChatsAdapter(ArrayList<User> Chats, OnChatsItemClickListener Listener) {
        mChats = Chats;
        mOnChatsItemClickListener = Listener;
    }

    @NonNull
    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.chats_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ViewHolder viewHolder, int i) {
        /*viewHolder.profilePic.setImageBitmap(mChats.get(i).getProfilePic());
        if(mChats.get(i).getProfilePic() == null) {
            Log.d("test", "onBindViewHolder: null");
        }
        viewHolder.username.setText(mChats.get(i).getName());*/
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView profilePic;
        TextView username;

        ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profilePic = itemView.findViewById(R.id.imgProfile);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnChatsItemClickListener.onChatsItemClick(getAdapterPosition());
        }

    }
    public interface OnChatsItemClickListener {
        void onChatsItemClick(int clickedItemIndex);
    }


    public static class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

        Uri uri;
        DatePicker datePicker;

        private final Integer PICK_IMAGE_REQUEST=1;
        private static final String TAG = ProfileActivity.class.getName();

        //final Fragment fragment1 = new HomeFragment();
        final Fragment fragment2 = new ChatFragment();
        final Fragment fragment3 = new BookingsFragment();
        final FragmentManager fm = getSupportFragmentManager();
        //Fragment active = fragment1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            Toolbar toolbar = findViewById(R.id.toolbar_profile_setup);
            setSupportActionBar(toolbar);

            /*datePicker = findViewById(R.id.datePicker);

            Spinner spinner = findViewById(R.id.spCountry);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.countries_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);*/

        }

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void chooseImg(View view) {
            Intent intent = new Intent();

            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                uri = data.getData();
                Intent i = new Intent(getApplicationContext(), CropImageActivity.class);
                i.setData(uri);
                i.putExtra("origin", "Profile");
                startActivity(i);
            }
        }
    }
}
