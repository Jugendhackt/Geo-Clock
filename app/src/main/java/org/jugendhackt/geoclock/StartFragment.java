package org.jugendhackt.geoclock;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;

import static org.jugendhackt.geoclock.R.layout.start;

public class StartFragment extends Fragment {

    String ADRESSE = "adresse";
    String RADIUS = "radius";
    String GEOFENCEID = "geofenceid";
    String wertAdresse;
    int wertRadius;
    double wertLänge = 48.396489;
    double wertBreite = 9.990495;

    Geofence mGeofence;
    PendingIntent mGeofencePendingIntent;
    private GeofencingClient mGeofencingClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        wertAdresse = preferences.getString(ADRESSE, "keine Adresse eingestellt");
        wertRadius = preferences.getInt(RADIUS, 1000);

        View rootView = inflater.inflate(start, container, false);

        /*TextView adressView = rootView.findViewById(R.id.textView3);
        adressView.setText(wertAdresse);

        TextView radiusView = rootView.findViewById(R.id.textView4);
        radiusView.setText(wertRadius + " km");
**/

                mGeofencingClient = LocationServices.getGeofencingClient(getActivity());

                 mGeofence = new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.
                        .setRequestId(GEOFENCEID)

                        .setCircularRegion(
                                wertLänge,
                                wertBreite,
                                wertRadius
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build();
                mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CharSequence text = "Die Falle ist jetzt scharf";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(getActivity(), text, duration);
                                toast.show();


                            }
                        })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CharSequence text = "eine Platzpatrone";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(getActivity(), text, duration);
                                toast.show();

                                throw new RuntimeException(e);
                            }
                        });




        final Button go = rootView.findViewById(R.id.button3);
        final Button help = rootView.findViewById(R.id.helpbtn);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new NewClockFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new HelpFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }


    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(mGeofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), AlarmActivity.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        return mGeofencePendingIntent;

    }

}
