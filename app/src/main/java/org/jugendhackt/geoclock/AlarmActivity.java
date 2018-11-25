package org.jugendhackt.geoclock;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Arrays;

public class AlarmActivity extends AppCompatActivity {
    MediaPlayer ringtone;

    private GeofencingClient mGeofencingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences einstellungen=PreferenceManager.getDefaultSharedPreferences(this);
        String tonton=einstellungen.getString("tonton", null);
        if (tonton!=null){
            Uri uri=Uri.parse(tonton);
            ringtone=new MediaPlayer();
            ringtone.setLooping(true);
            if (Build.VERSION.SDK_INT >= 21){
                ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
            }
            else {
                ringtone.setAudioStreamType(AudioManager.STREAM_ALARM);
            }
            try {
                ringtone.setDataSource(this, uri);
                ringtone.prepare();
                ringtone.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Button button_stop=findViewById(R.id.button_stop);
        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        mGeofencingClient.removeGeofences(Arrays.asList(new String[] {NewClockFragment.GEOFENCEID}))
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //TODO
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       //TODO
                    }
                });
    }
    @Override
    public void onStop(){
        if(ringtone!=null) {
            ringtone.stop();
        }
            super.onStop();
    }
}
