package daynight.daynnight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.List;
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
    private Button boutonCenter;
    private boolean LOCALISATION_UPDATE = true;
    private boolean MAP_CENTREE = true;
    private TranslateAnimation translateAnimation;
    private ImageView imageViewPersonnage;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        move = new float[1];
        prevPos = new LatLng(0, 0);

        imageViewPersonnage = (findViewById(R.id.imageViewPersonnage));

        boutonCenter = (findViewById(R.id.boutonCenter));
        boutonCenter.setClickable(true);
        boutonCenter.setVisibility(View.VISIBLE);
        boutonCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOCALISATION_UPDATE = true;
                MAP_CENTREE = true;
                boutonCenter.setVisibility(View.INVISIBLE);
            }
        });

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

        if (locationManager != null) locationManager.removeUpdates(locationListener);
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

    /*private int getDrawable(int race, int frame){
        return this.getResources().getIdentifier("arthur" + race + "_" + frame + ".png", "drawable", this.getPackageName());
    }*/


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //Stylisation de la carte avec JSON d'un Raw.xml
        try {

            boolean reussi = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

            if (!reussi)
                Log.e("MapsActivity", "Génération du style impossible");
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Impossible de trouver le style", e);
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    livePos = new LatLng(location.getLatitude(), location.getLongitude());

                    final View persomarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

                    Log.d("POS", livePos.toString());
                    //Distance entre la position actuelle et la dernière actualisation
                    Location.distanceBetween(prevPos.latitude, prevPos.longitude, livePos.latitude, livePos.longitude, move);
                    Log.d("Move", String.valueOf(move[0]));

                    //Si la distance entre deux actualisation est supérieure à 3m, alors le personnage se déplace
                    if (move[0] > 3) {


                        if(MAP_CENTREE){
                            //map.moveCamera(CameraUpdateFactory.newLatLng(livePos));
                            map.animateCamera(CameraUpdateFactory.newLatLng(livePos), (int) move[0] / 5, null);
                        }
                        else{
                            translateAnimation = new TranslateAnimation(imageViewPersonnage.getX(), map.getProjection().toScreenLocation(livePos).x, imageViewPersonnage.getY(), map.getProjection().toScreenLocation(livePos).y);
                            translateAnimation.setDuration(5000);
                            translateAnimation.setFillAfter(true);
                            translateAnimation.start();
                        }

                        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                               @Override
                               public void onCameraMoveStarted(int reason) {
                                   LOCALISATION_UPDATE = false;
                                   if(reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE){
                                       map.stopAnimation();
                                       boutonCenter.setClickable(true);
                                       boutonCenter.setVisibility(View.VISIBLE);
                                   }
                               }
                           });

                            Log.d("Location changed", "location changed");

                            countDownTimer = new CountDownTimer(5000, 200) {
                            private int count = 0;
                            @Override
                            public void onTick(long millisUntilFinished) {
                                ++count;
                                String temp = "arthur1_" + count%6;
                                imageViewPersonnage.setImageResource(imageViewPersonnage.getContext().getResources().getIdentifier(temp, "drawable", imageViewPersonnage.getContext().getPackageName()));
                                Log.d("icon changed", "icon changed");
                            }

                            @Override
                            public void onFinish() {

                            }
                        };


                            map.moveCamera(CameraUpdateFactory.newLatLng(livePos));
                    }

                        prevPos = livePos;
                }


                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    Log.d("status", "status changed: " + s);
                }

                @Override
                public void onProviderEnabled(String s) {
                    Log.d("enabled provider", "provider enabled: " + s + "\nLast loc:"/* + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)*/);
                }

                @Override
                public void onProviderDisabled(String s) {

                    Log.d("disabled provider", "provider disabled: " + s);
                }
            });


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
