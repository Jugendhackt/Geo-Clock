package org.jugendhackt.geoclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.session.MediaSession;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
   MapView Auswahlkarte;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = null;
   SharedPreferences Speicher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Speicher=PreferenceManager.getDefaultSharedPreferences(ctx);
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);
        Auswahlkarte = (MapView) findViewById(R.id.map);
        Auswahlkarte.setTileSource(TileSourceFactory.MAPNIK);
        Auswahlkarte.setBuiltInZoomControls(true);
        Auswahlkarte.setMultiTouchControls(true);
        IMapController mapController = Auswahlkarte.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(Speicher.getFloat("lat",48.39651f),Speicher.getFloat("long",9.9904199f));
        mapController.setCenter(startPoint);
        Markeranzeige(startPoint.getLatitude(),startPoint.getLongitude());
        Button FertigKnopf=findViewById(R.id.fertig_knopf);
        FertigKnopf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });


        Overlay touchOverlay = new Overlay(this){
            @Override
            public void draw(Canvas arg0, MapView arg1, boolean arg2) {

            }
            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int)e.getX(), (int)e.getY());
                Float longitude = (((float)loc.getLongitudeE6())/1000000);
                Float latitude = (((float)loc.getLatitudeE6())/1000000);
                System.out.println("- Latitude = " + latitude + ", Longitude = " + longitude );



                Speicher.edit().putFloat("long",longitude).putFloat("lat",latitude).apply();
                Markeranzeige(latitude,longitude);
                return true;
            }
        };
        Auswahlkarte.getOverlays().add(touchOverlay);
    }
    void Markeranzeige(double latitude,double longitude){
        final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_marker);
        ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
        OverlayItem mapItem = new OverlayItem("", "", new GeoPoint(latitude,longitude));
        mapItem.setMarker(marker);
        overlayArray.add(mapItem);
        if(anotherItemizedIconOverlay==null){
            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray,null);
            Auswahlkarte.getOverlays().add(anotherItemizedIconOverlay);
            Auswahlkarte.invalidate();
        }else {
            Auswahlkarte.getOverlays().remove(anotherItemizedIconOverlay);
            Auswahlkarte.invalidate();
            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
            Auswahlkarte.getOverlays().add(anotherItemizedIconOverlay);
        }
    }
    public void onResume(){
        super.onResume();
        Auswahlkarte.onResume();
    }

    public void onPause(){
        super.onPause();
        Auswahlkarte.onPause();
    }
}