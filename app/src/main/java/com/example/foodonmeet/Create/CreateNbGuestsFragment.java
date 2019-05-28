package com.example.foodonmeet.Create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodonmeet.R;

public class CreateNbGuestsFragment extends Fragment {

    private ImageButton btnMinus;
    private ImageButton btnPlus;
    private TextView tvNbGuests;

    private int nb;

    public CreateNbGuestsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_nb_guests, container, false);

        nb = 3;

        btnMinus = view.findViewById(R.id.btnMinus);
        btnPlus = view.findViewById(R.id.btnPlus);
        tvNbGuests = view.findViewById(R.id.tvNbGuests);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nb == 1) {
                    btnMinus.setBackground(getResources().getDrawable(R.drawable.shape_button_nb_guests_disabled));
                }
                if(nb > 0) {
                    nb--;
                    tvNbGuests.setText(Integer.toString(nb));
                }


            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nb == 0) {
                    btnMinus.setBackground(getResources().getDrawable(R.drawable.shape_button_nb_guests));
                }
                nb++;
                tvNbGuests.setText(Integer.toString(nb));
            }
        });

        return view;
    }

    public int getNbGuests() {
        return nb;
    }
}
