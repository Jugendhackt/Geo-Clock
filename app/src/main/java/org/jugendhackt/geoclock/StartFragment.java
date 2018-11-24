package org.jugendhackt.geoclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static org.jugendhackt.geoclock.R.layout.start;

public class StartFragment extends Fragment {

    String ADRESSE = "adresse";
    String RADIUS = "radius";
    String wertAdresse;
    int wertRadius;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        wertAdresse = preferences.getString(ADRESSE, "keine Adresse eingestellt");
        wertRadius = preferences.getInt(RADIUS, 0);

        View rootView = inflater.inflate(start, container, false);
        TextView adressView = rootView.findViewById(R.id.textView3);
        adressView.setText(wertAdresse);

        TextView radiusView = rootView.findViewById(R.id.textView4);
        radiusView.setText(wertRadius + " km");

        return rootView;
    }

}
