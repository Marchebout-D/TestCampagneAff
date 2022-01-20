package com.example.carte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager = null;
    private String fournisseur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapView map;
        ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();

        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);//recuperation de la map dans le layout
        map.setTileSource(TileSourceFactory.MAPNIK);//render la forme de la carte
        map.setBuiltInZoomControls(true);//zoomable
        GeoPoint startPoint = new GeoPoint(45.936698, -0.961697);//coord rochefort objet de coor
        IMapController mapController = map.getController();//initialisation du controleur de la carte
        mapController.setCenter(startPoint); //centrer la carte sur rochefort
        mapController.setZoom(18);//mettre le zoom de base
//creation d'un point
        Marker point1 = new Marker(map);
        GeoPoint point1position = new GeoPoint(46.160329, -1.151139);//la rochelle
        point1.setPosition(point1position);
        //Drawable image = ContextCompat.getDrawable(getApplication(), R.drawable.markerosm);
        Drawable image2 = this.getApplicationContext().getDrawable(R.drawable.ic_action_name);
        point1.setIcon(image2);
        point1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(point1);
        Log.i("marker", point1.toString());
        Log.i("marker", point1.getPosition().toString());
        //note les marker feront l'objet d'une classe dans le vrai projet
//geolocalisation de soi
        initialiserLocalisation();


    }

    private void initialiserLocalisation() {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteres = new Criteria();

            // la précision  : (ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision)
            criteres.setAccuracy(Criteria.ACCURACY_FINE);

            // l'altitude
            criteres.setAltitudeRequired(true);

            // la direction
            criteres.setBearingRequired(true);

            // la vitesse
            criteres.setSpeedRequired(true);

            // la consommation d'énergie demandée
            criteres.setCostAllowed(true);
            criteres.setPowerRequirement(Criteria.POWER_HIGH);

            fournisseur = locationManager.getBestProvider(criteres, true);
            Log.i("GPS", "fournisseur : " + fournisseur);
        }

        if (fournisseur != null) {
            // dernière position connue
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location localisation = locationManager.getLastKnownLocation(fournisseur);
            Log.i("GPS", "localisation : " + localisation.toString());
            String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
            Log.i("GPS", "coordonnees : " + coordonnees);
            String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
            Log.i("GPS", autres);
            //String timestamp = String.format("Timestamp : %d\n", localisation.getTime());
            //Log.d("GPS", "timestamp : " + timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date(localisation.getTime());
            Log.i("GPS", sdf.format(date));
        }
    }


}