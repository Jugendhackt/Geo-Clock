package org.jugendhackt.geoclock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static org.jugendhackt.geoclock.R.layout.start;

public class NewClockFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Sicht= inflater.inflate(R.layout.wecker, container, false);
        Button KartenKnopf=Sicht.findViewById(R.id.button_map);
        KartenKnopf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(),MapActivity.class));
            }
        });

        Button StartKnopf = Sicht.findViewById(R.id.button_start);
        StartKnopf.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getActivity(),AlarmActivity.class));
                    }
                }, 20000);
            }
        })
        ;

        return Sicht;
    }


}
