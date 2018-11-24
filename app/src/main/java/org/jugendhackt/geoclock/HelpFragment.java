package org.jugendhackt.geoclock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static org.jugendhackt.geoclock.R.layout.anleitung;
import static org.jugendhackt.geoclock.R.layout.start;

public class HelpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(anleitung, container, false);
    }


}
