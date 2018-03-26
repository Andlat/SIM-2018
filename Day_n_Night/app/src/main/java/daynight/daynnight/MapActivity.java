package daynight.daynnight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final int LOCALISATION_REQUEST = 1;
    private GoogleMap map;
    private LocationManager locationManager;
    private LatLng livePos;
    private LatLng prevPos;
    private LatLng poiUpdate;
    private float[] distanceFromPoiUpdate;
    private float[] move;
    private float[] distanceFromPoi;
    private LocationListener locationListener;
    private Button boutonCenter;
    private boolean MAP_CENTREE = true;
    private TranslateAnimation translateAnimation;
    private ImageView imageViewPersonnage;
    private Display display;
    private Point size;
    private Marker persoMarker;
    private AnimationDrawable animationDrawable;
    private JSONObject jsonPOI;
    private JSONArray jsonResults;
    private ArrayList posPOI;
    private LatLng tempPos;
    private RelativeLayout.LayoutParams layoutParams;
    private int nbrPage;
    private String pageToken;
    private String[] filters;
    private String directionRegardee = "droite";
    private URL url;
    Intent intent;
    private int modelePerosnage = 1;
    private MarkerOptions POImarker;
    private BitmapDrawable bitmapDrawable;
    private Bitmap smallMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        try {
            intent = getIntent();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        imageViewPersonnage.setBackgroundResource(getResources().getIdentifier("mapcharacteranimation" + modelePerosnage, "drawable", MapActivity.this.getPackageName()));
        animationDrawable = (AnimationDrawable)imageViewPersonnage.getBackground();

        bitmapDrawable = (BitmapDrawable)getResources().getDrawable(getResources().getIdentifier("arthur" + modelePerosnage + "_1", "drawable", MapActivity.this.getPackageName()));

        smallMarker = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), imageViewPersonnage.getWidth(), imageViewPersonnage.getHeight(), false);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = (size.x/2)-(imageViewPersonnage.getWidth()/2);
        layoutParams.topMargin = (size.y/2)-(imageViewPersonnage.getHeight()/2);

        filters = new String[]{"airport", "amusement_park", "aquarium", "art_gallery", "campground", "casino", "church", "city_hall", "courthouse", "embassy", "hindu_temple", "hospital", "library", "lodging", "mosque", "museum", "park", /*"school",*/ "stadium", "synagogue", "university", "zoo"};

        boutonCenter = (findViewById(R.id.boutonCenter));
        boutonCenter.setClickable(true);
        boutonCenter.setVisibility(View.INVISIBLE);
        boutonCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(persoMarker != null){
                    persoMarker.setVisible(false);
                }
                MAP_CENTREE = true;
                boutonCenter.setVisibility(View.INVISIBLE);
                if(livePos != null){
                    map.moveCamera(CameraUpdateFactory.newLatLng(livePos));
                    //imageViewPersonnage.setScaleType(ImageView.ScaleType.CENTER);
                    imageViewPersonnage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    //imageViewPersonnage.setX((Resources.getSystem().getDisplayMetrics().widthPixels/2)-(imageViewPersonnage.getMaxWidth()/2));
                    //imageViewPersonnage.setY((Resources.getSystem().getDisplayMetrics().heightPixels/2)-(imageViewPersonnage.getMaxHeight()/2));
                    imageViewPersonnage.setVisibility(View.VISIBLE);
                }
            }
        });

        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if(reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE){
                    Log.d("MapMovement", "Cause: Gesture");
                    imageViewPersonnage.setVisibility(View.INVISIBLE);
                    MAP_CENTREE = false;
                    boutonCenter.setClickable(true);
                    boutonCenter.setVisibility(View.VISIBLE);
                    if(livePos != null){
                        if(persoMarker == null){
                            persoMarker = map.addMarker(new MarkerOptions()
                                    .position(livePos).icon(BitmapDescriptorFactory
                                            .fromBitmap(smallMarker)));
                        }
                        persoMarker.setPosition(livePos);
                        persoMarker.setVisible(true);
                    }
                    else{
                        persoMarker = map.addMarker(new MarkerOptions()
                                .position(livePos).icon(BitmapDescriptorFactory
                                        .fromBitmap(smallMarker)));
                    }
                }
                else{
                    Log.d("MapMovement", "Cause: Code");
                    imageViewPersonnage.setVisibility(View.VISIBLE);
                    animationDrawable.start();
                }
            }
        });

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if(persoMarker != null){
                    persoMarker.setVisible(false);
                    imageViewPersonnage.setX(map.getProjection().toScreenLocation(livePos).x);
                    imageViewPersonnage.setY(map.getProjection().toScreenLocation(livePos).y);
                    imageViewPersonnage.setVisibility(View.VISIBLE);
                }
                animationDrawable.stop();
            }
        });

        move = new float[1];
        distanceFromPoiUpdate = new float[1];
        prevPos = new LatLng(0, 0);
        poiUpdate = new LatLng(0,0);
        posPOI = new ArrayList<LatLng>();

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
                    imageViewPersonnage.setVisibility(View.VISIBLE);
                    livePos = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d("Localisation", "Recue: " + livePos.toString());

                    if(prevPos != null && livePos != null){
                        //if(livePos.longitude - prevPos.longitude < 0 && directionRegardee == "droite"){
                        if(map.getProjection().toScreenLocation(livePos).x - map.getProjection().toScreenLocation(prevPos).x < 0 && directionRegardee == "droite"){
                            int i= map.getProjection().toScreenLocation(livePos).x;
                            imageViewPersonnage.setScaleX(-1);
                            directionRegardee = "gauche";
                        }else if(map.getProjection().toScreenLocation(livePos).x - map.getProjection().toScreenLocation(prevPos).x > 0 && directionRegardee == "gauche"){
                            imageViewPersonnage.setScaleX(1);
                            directionRegardee = "droite";
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
                        if(persoMarker!= null){
                            persoMarker.setVisible(false);
                        }
                        if(MAP_CENTREE){
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(livePos, 19));

                        } else{
                            translateAnimation = new TranslateAnimation(imageViewPersonnage.getX(),
                                    map.getProjection().toScreenLocation(livePos).x- imageViewPersonnage.getX(),
                                    imageViewPersonnage.getY(),
                                    map.getProjection().toScreenLocation(livePos).y - imageViewPersonnage.getY());
                            translateAnimation.setRepeatCount(0);
                            //translateAnimation.setFillBefore(true);
                            translateAnimation.setFillAfter(true);
                            translateAnimation.setDuration(5000);
                            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    imageViewPersonnage.setVisibility(View.VISIBLE);
                                    animationDrawable.start();
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    animationDrawable.stop();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    //translateAnimation.cancel();
                                    //animation.cancel();
                                    imageViewPersonnage.clearAnimation();
                                }
                            });
                            imageViewPersonnage.setAnimation(translateAnimation);
                            imageViewPersonnage.startAnimation(translateAnimation);
                            //translateAnimation.start();
                        }

                        Log.d("Location changed", "location changed");
                    }

                    //Va chercher les coordonnés des poi
                    Location.distanceBetween(poiUpdate.latitude, poiUpdate.longitude, livePos.latitude, livePos.longitude, distanceFromPoiUpdate);
                    if(distanceFromPoiUpdate[0] > 20000){


                        for (String filter : filters) {
                            nbrPage = 0;
                            try {
                                do {
                                    Log.d("Request", "Entering page " + nbrPage);
                                    final ExecutorService executor = Executors.newSingleThreadExecutor();
                                    HttpRequest request = null;
                                    try {
                                        if (nbrPage == 0) {
                                            request = new HttpRequest(url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + livePos.latitude + "," + livePos.longitude + "&type=" + filter + "&rankby=distance&sensor=false&key=AIzaSyCkJvT6IguUIXVbBAe8-0l2vO1RWbxW4Tk"));

                                        } else {
                                            request = new HttpRequest(url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + pageToken + "&key=AIzaSyCkJvT6IguUIXVbBAe8-0l2vO1RWbxW4Tk"));
                                        }
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    final FutureTask<String> future = new FutureTask<>(request);

                                    executor.execute(future);
                                    String response = null;

                                    response = future.get();
                                    jsonPOI = new JSONObject(response);
                                    jsonResults = jsonPOI.getJSONArray("results");

                                    Log.d("Request", jsonPOI.toString());

                                    //Enregistre tout les position des POI dans le rayon spécifié
                                    for (int k = 0; k < jsonResults.length(); k++) {

                                        posPOI.add(new LatLng(jsonResults.getJSONObject(k).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                                jsonResults.getJSONObject(k).getJSONObject("geometry").getJSONObject("location").getDouble("lng")));

                                        tempPos = (LatLng) posPOI.get(posPOI.size() - 1);
                                        map.addMarker(POImarker = new MarkerOptions().title("POI")
                                                .position(tempPos).icon(BitmapDescriptorFactory
                                                        .fromResource(R.drawable.chest)));

                                        Log.d("Request", "json " + k + " " + nbrPage);
                                    }


                                    pageToken = jsonPOI.getString("next_page_token");
                                    nbrPage++;
                                    Log.d("Request", pageToken);
                                    Log.d("Request", url.toString());
                                    Log.d("Request", String.valueOf(jsonPOI.getJSONArray("results").length()));
                                } while (jsonPOI.getJSONArray("results").length() == 20);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("Request", "Malformed url");
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                Log.d("Request", "NOPE ça marche pas");
                            }
                        }
                        poiUpdate = livePos;
                    }
                    prevPos = livePos;
                }

                @Override
                public void onStatusChanged (String s,int i, Bundle bundle){
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

        //Réaction au clique sur un marqueur
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                distanceFromPoi = new float[1];
                Location.distanceBetween(livePos.latitude,livePos.longitude,marker.getPosition().latitude,marker.getPosition().longitude, distanceFromPoi);

                if(marker.getTitle().equals("POI") && distanceFromPoi[0]<100){

                    Toast.makeText(MapActivity.this, "HEHE", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
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
