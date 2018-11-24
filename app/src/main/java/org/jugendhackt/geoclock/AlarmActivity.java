package org.jugendhackt.geoclock;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity {
    MediaPlayer ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
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
    }
    @Override
    public void onStop(){
        ringtone.stop();
        super.onStop();
    }
}
