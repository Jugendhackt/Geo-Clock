package org.jugendhackt.geoclock;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static org.jugendhackt.geoclock.R.layout.start;

public class NewClockFragment extends Fragment {

    String LAT = "lat";
    String LON = "long";
    String RADIUS = "radius";
    public static String GEOFENCEID = "geofenceid";
    String wertAdresse;
    int wertRadius;
    double wertLänge;
    double wertBreite;

    Geofence mGeofence;
    PendingIntent mGeofencePendingIntent;
    private GeofencingClient mGeofencingClient;


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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        wertRadius = preferences.getInt(RADIUS, 1000);
        wertLänge = preferences.getFloat(LON, (float)48.396489);
        wertBreite = preferences.getFloat(LAT, (float)9.990495);

        wertAdresse = wertLänge + "," + wertBreite;


        return Sicht;
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
