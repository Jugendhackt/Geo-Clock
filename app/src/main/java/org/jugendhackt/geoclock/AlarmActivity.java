package org.jugendhackt.geoclock;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class AlarmActivity extends AppCompatActivity {
    Ringtone ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        SharedPreferences einstellungen=PreferenceManager.getDefaultSharedPreferences(this);
        String tonton=einstellungen.getString("tonton", null);
        if (tonton!=null){
            Uri uri=Uri.parse(tonton);
            ringtone=RingtoneManager.getRingtone(this, uri);
            ringtone.play();
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
