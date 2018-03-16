package daynight.daynnight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private static final int LOCALISATION_REQUEST = 1;
    private GoogleMap map;
    private LocationManager locationManager;
    private LatLng livePos;
    private LatLng prevPos;
    private LatLng poiUpdate;
    private float[] distanceFromPoiUpdate;
    private float[] move;
    private LocationListener locationListener;
    private Button boutonCenter;
    private boolean MAP_CENTREE = true;
    private TranslateAnimation translateAnimation;
    private ImageView imageViewPersonnage;
    private Display display;
    private Point size;
    private Marker persoMarker;
    private AnimationDrawable animationDrawable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        imageViewPersonnage = (findViewById(R.id.imageViewPersonnage));
        imageViewPersonnage.setBackgroundResource(R.drawable.mapcharacteranimation1);
        animationDrawable1 = (AnimationDrawable)imageViewPersonnage.getBackground();

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        boutonCenter = (findViewById(R.id.boutonCenter));
        boutonCenter.setClickable(true);
        boutonCenter.setVisibility(View.INVISIBLE);
        boutonCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MAP_CENTREE = true;
                boutonCenter.setVisibility(View.INVISIBLE);
                map.moveCamera(CameraUpdateFactory.newLatLng(livePos));
                imageViewPersonnage.setX((size.x/2) - imageViewPersonnage.getWidth()/2);
                imageViewPersonnage.setY((size.y/2) - imageViewPersonnage.getHeight()/2);
            }
        });

        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if(reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE){
                    Log.d("MapMovement", "Cause: Gesture");
                    boutonCenter.setClickable(true);
                    boutonCenter.setVisibility(View.VISIBLE);
                    persoMarker = map.addMarker(new MarkerOptions()
                            .position(livePos).icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.arthur1_1)));
                    imageViewPersonnage.setVisibility(View.INVISIBLE);
                }
                else{
                    Log.d("MapMovement", "Cause: Code");
                    animationDrawable1.start();
                }
            }
        });

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                animationDrawable1.stop();
            }
        });

        move = new float[1];
        distanceFromPoiUpdate = new float[1];
        prevPos = new LatLng(0, 0);
        poiUpdate = new LatLng(0,0);

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
                    Log.d("Localisation", "Recue: " + livePos.toString());

                    //Va chercher les coordonnés des poi dans un rayon de 50km
                    Location.distanceBetween(poiUpdate.latitude, poiUpdate.longitude, livePos.latitude, livePos.longitude, distanceFromPoiUpdate);
                    if(distanceFromPoiUpdate[0] > 20000){
                        final ExecutorService executor = Executors.newSingleThreadExecutor();

                        final HttpRequest request = new HttpRequest();
                        final FutureTask<String> future = new FutureTask<>(request);

                        executor.execute(future);
                        String response = null;

                        try {
                             response = future.get();
                            Log.d("Request", response);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            Log.d("Request", "NOPE ça marche pas");
                        }
                    }

                    Log.d("POS", livePos.toString());
                    //Distance entre la position actuelle et la dernière actualisation
                    Location.distanceBetween(prevPos.latitude, prevPos.longitude, livePos.latitude, livePos.longitude, move);
                    Log.d("Move", String.valueOf(move[0]));

                    Location prevLocation = new Location("");
                    prevLocation.setLatitude(prevPos.latitude);
                    prevLocation.setLongitude(prevPos.longitude);

                    Location presentLocation = new Location("");
                    presentLocation.setLatitude(livePos.latitude);
                    presentLocation.setLongitude(livePos.longitude);

                    //Si la distance entre deux actualisations est supérieure à 2m, alors le personnage se déplace
                    if (prevLocation.distanceTo(presentLocation) > 2) {

                        if(MAP_CENTREE){
                            map.clear();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(livePos, 19));
                            //map.moveCamera(CameraUpdateFactory.newLatLng(livePos));
                            /*translateAnimation = new TranslateAnimation((float)prevLocation.getLongitude(),
                                    map.getProjection().toScreenLocation(livePos).x,
                                    (float)prevLocation.getLatitude(),
                                    map.getProjection().toScreenLocation(livePos).y);
                            translateAnimation.setDuration(5000);
                            translateAnimation.setFillBefore(true);
                            translateAnimation.setFillAfter(true);
                            imageViewPersonnage.setAnimation(translateAnimation);*/
                            /*map.animateCamera(CameraUpdateFactory.newLatLng(livePos),
                                    (int) move[0] / 5000, null);*/
                            /*translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    animationDrawable1.start();
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    animationDrawable1.stop();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            translateAnimation.start();*/
                        }
                        else{
                            translateAnimation = new TranslateAnimation(imageViewPersonnage.getX(),
                                    map.getProjection().toScreenLocation(livePos).x,
                                    imageViewPersonnage.getY(),
                                    map.getProjection().toScreenLocation(livePos).y);
                            translateAnimation.setDuration(5000);
                            translateAnimation.setFillBefore(true);
                            translateAnimation.setFillAfter(true);
                            imageViewPersonnage.setAnimation(translateAnimation);
                            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    animationDrawable1.start();
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    animationDrawable1.stop();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            translateAnimation.start();
                        }

                        Log.d("Location changed", "location changed");

                        map.moveCamera(CameraUpdateFactory.newLatLng(livePos));
                        /*map.animateCamera(CameraUpdateFactory.newLatLng(livePos),
                                (int) move[0] / 5000, null);*/
                        //animationDrawable1.start();
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
