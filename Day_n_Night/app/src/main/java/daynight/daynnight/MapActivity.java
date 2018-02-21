package daynight.daynnight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;
import java.util.Objects;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private static final int LOCALISATION_REQUEST = 1;
    private GoogleMap map;
    private LocationManager locationManager;
    private LatLng livePos;
    private LatLng prevPos;
    private float[] move;
    private LocationListener locationListener;
    private Marker perso;

//PUTE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        move = new float[1];
        prevPos = new LatLng(0, 0);


        //Si la permission de localisation n'est pas donné une fenêtre la demande
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Si la permission a deja été refusé, affiche un fenêtre d'explication et redemande la permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                builder.setMessage(R.string.rationale_dialog);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCALISATION_REQUEST);
                    }
                });
                builder.show();

            //Si c'est la première fois, on demande directement l'autorisation a l'utilisateur
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCALISATION_REQUEST);
            }
        }
    }


    /**
     *Arrete la demande de localisation lorsque l'app n'est pas active
     */
    @Override
    public void onPause() {
        super.onPause();

        if(locationManager != null) locationManager.removeUpdates(locationListener);
    }


    /**
     * Rappel la fonction OnMapReady() lorsque l'app devient active
     */
    @Override
    protected void onResume() {
        super.onResume();

        //Quand la map est disponible, on appel la fonction OnMapReady()
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //Stylisation de la carte avec JSON d'un Raw.xml
        try {

            boolean reussi = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

            if(!reussi)
                Log.e("MapsActivity", "Génération du style impossible");
        }
        catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Impossible de trouver le style", e);
        }


        try {
            //Actulise la position sur la carte à chaque x ms
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    livePos = new LatLng(location.getLatitude(), location.getLongitude());

                    //Distance entre la position actuelle et la dernière actualisation
                    Location.distanceBetween(prevPos.latitude, prevPos.longitude, livePos.latitude, livePos.longitude, move);
                    Log.d("Move", String.valueOf(move[0]));

                    //Si la distance entre deux actualisation est suppérieur à 3m alors le personnage se déplace
                    if(move[0] > 3){

                        if(perso != null) perso.remove();

                        perso = map.addMarker(new MarkerOptions()
                                .position(livePos)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icondude)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(livePos));
                    }

                    prevPos = livePos;
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        //Si nous avons pas accès à la localisation
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        map.setOnPoiClickListener(this);
    }


    /**
     * Réaction au clique d'un POI
     * @param poi   Le POI qui a été cliqué
     */
    @Override
    public void onPoiClick(PointOfInterest poi) {

        float[] distancePOI = new float[1];

        Toast.makeText(getApplicationContext(), "BISCUIT!!!!!!", Toast.LENGTH_SHORT).show();
        Location.distanceBetween(livePos.latitude, livePos.longitude, poi.latLng.latitude, poi.latLng.longitude, distancePOI);
        Log.d("Distance", String.valueOf(distancePOI[0]));
    }

    /**
     * gère les réponses des demandes de permission
     * @param requestCode   Code qui indique de quel autorisation il s'agit
     * @param permissions
     * @param grantResults  Réponse de l'utilisateur
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == LOCALISATION_REQUEST) {
            //Si l'autorisation de géolocalisation n'est pas donné,
            //on averti l'utilisateur que la fonction de jour est désactivé
            if (permissions.length != 1
                    || !Objects.equals(permissions[0], Manifest.permission.ACCESS_FINE_LOCATION)
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "Le jeu de jour a été désactivé.\nActivez la géolocalisation dansles réglages pour l'activer!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
