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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static daynight.daynnight.MainActivity.SurChangementActivity;
import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.onPause;


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
    private Poi tempPoi;
    private LatLng tempPos;
    private RelativeLayout.LayoutParams layoutParams;
    private int nbrPage;
    private String pageToken;
    private String[] filters;
    private String directionRegardee = "droite";
    private URL url;
    Intent intent;
    private int modelePerosnage = 1;
    private Marker tempMarker;
    private BitmapDrawable bitmapDrawable;
    private Bitmap smallMarker;
    private Poi actualPoi;
    private ArrayList<String> idPois;
    private boolean addPoi;
    private String tempId;

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

        //Listner du bouton de pause
        ImageButton pause = (ImageButton) findViewById(R.id.pauseJour);
        pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MapActivity.this, PopupPause.class));
                SurChangementActivity = true;
            }
        });

        //Initialisation de variables
        distanceFromPoiUpdate = new float[1];
        prevPos = new LatLng(0, 0);
        livePos = new LatLng(0,0);
        poiUpdate = new LatLng(0,0);

    }


    /**
     *Arrete la demande de localisation lorsque l'app n'est pas active
     */
    @Override
    public void onPause()
    {
        super.onPause();

        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onStop()
    {
        if(SurChangementActivity)
        {
            MainActivity.musiqueDeFond.pause();
            MainActivity.ma.sauvegardeJoueur(joueur);
        }
        super.onStop();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SurChangementActivity = false;
    }

    /**
     * Rappel la fonction OnMapReady() lorsque l'app devient active
     */
    @Override
    protected void onResume()
    {
        if(MainActivity.joueur.getMusique())
        {
            MainActivity.musiqueDeFond.start();
        }
        super.onResume();

        //Quand la map est disponible, on appel la fonction OnMapReady()
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Initialisation de variables
        final RelativeLayout loading = findViewById(R.id.loadingPanel);
        loading.setVisibility(View.VISIBLE);

        map = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        imageViewPersonnage = (findViewById(R.id.imageViewPersonnage));
        imageViewPersonnage.setBackgroundResource(getResources().getIdentifier("mapcharacteranimation" + getResources().getResourceEntryName(MainActivity.joueur.getSkin()).replace("arthur", "").replace("_1", ""), "drawable", MapActivity.this.getPackageName()));
        animationDrawable = (AnimationDrawable)imageViewPersonnage.getBackground();

        //Creation du marqueur qui sera sur la carte, selon celui sélectionné
        bitmapDrawable = (BitmapDrawable)getResources().getDrawable(MainActivity.joueur.getSkin());
        smallMarker = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), imageViewPersonnage.getWidth(), imageViewPersonnage.getHeight(), false);

        //
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        /*layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = (size.x/2)-(imageViewPersonnage.getWidth()/2);
        layoutParams.topMargin = (size.y/2)-(imageViewPersonnage.getHeight()/2);*/

        //types de poi
        filters = new String[]{"airport", "amusement_park", "aquarium", "art_gallery", "campground", "casino", "church", "city_hall", "courthouse", "embassy", "hindu_temple", "hospital", "library", "lodging", "mosque", "museum", "park", /*"school",*/ "stadium", "synagogue", "university", "zoo"};

        idPois = new ArrayList<String>();
        //Recentrage de la carte lors du click sur le bouton Center
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
                    //Si la caméra bouge à cause d'un geste de l'utilisateur
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

        //Stylisation de la carte avec JSON d'un Raw.xml
        try {

            boolean reussi = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

            if (!reussi)
                Log.e("MapsActivity", "Génération du style impossible");
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Impossible de trouver le style", e);
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //imageViewPersonnage.setVisibility(View.VISIBLE);
                    livePos = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d("Localisation", "Recue: " + livePos.toString());

                    if (prevPos != null && livePos != null) {
                        //if(livePos.longitude - prevPos.longitude < 0 && directionRegardee == "droite"){
                        if (map.getProjection().toScreenLocation(livePos).x - map.getProjection().toScreenLocation(prevPos).x < 0 && directionRegardee == "droite") {
                            int i = map.getProjection().toScreenLocation(livePos).x;
                            imageViewPersonnage.setScaleX(-1);
                            directionRegardee = "gauche";
                        } else if (map.getProjection().toScreenLocation(livePos).x - map.getProjection().toScreenLocation(prevPos).x > 0 && directionRegardee == "gauche") {
                            imageViewPersonnage.setScaleX(1);
                            directionRegardee = "droite";
                        }
                    }

                    Location prevLocation = new Location("");
                    prevLocation.setLatitude(prevPos.latitude);
                    prevLocation.setLongitude(prevPos.longitude);

                    Location presentLocation = new Location("");
                    presentLocation.setLatitude(livePos.latitude);
                    presentLocation.setLongitude(livePos.longitude);

                    //Si la distance entre deux actualisations est supérieure à 2m, alors le personnage se déplace
                    if (prevLocation.distanceTo(presentLocation) > 2) {
                        if (persoMarker != null) {
                            persoMarker.setVisible(false);
                        }
                        if (MAP_CENTREE) {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(livePos, map.getCameraPosition().zoom));
                        } else {
                            translateAnimation = new TranslateAnimation(imageViewPersonnage.getX(),
                                    map.getProjection().toScreenLocation(livePos).x - imageViewPersonnage.getX(),
                                    imageViewPersonnage.getY(),
                                    map.getProjection().toScreenLocation(livePos).y - imageViewPersonnage.getY());
                            translateAnimation.setRepeatCount(0);
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
                                    imageViewPersonnage.clearAnimation();
                                }
                            });
                            imageViewPersonnage.setAnimation(translateAnimation);
                            imageViewPersonnage.startAnimation(translateAnimation);
                        }

                        Log.d("Location changed", "location changed");
                    }

                    //Va chercher les coordonnés des poi
                    Location.distanceBetween(poiUpdate.latitude, poiUpdate.longitude, livePos.latitude, livePos.longitude, distanceFromPoiUpdate);
                    if (distanceFromPoiUpdate[0] > 10000) {

                        //Utilise chacun des filtres dans l'url pour avoir chaque type de POI
                        for (String filter : filters) {
                            nbrPage = 0;
                            try {
                                do {
                                    final ExecutorService executor = Executors.newSingleThreadExecutor();
                                    HttpRequest request = null;
                                    try {
                                        if (nbrPage == 0) {
                                            //Si c'est la premiere page avec ce filtre, on utilise l'url normal
                                            request = new HttpRequest(url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + livePos.latitude + "," + livePos.longitude + "&type=" + filter + "&rankby=distance&sensor=false&key=AIzaSyCkJvT6IguUIXVbBAe8-0l2vO1RWbxW4Tk"));

                                        } else {
                                            //Sinon on utilise le page token fouri dans la page precedante
                                            request = new HttpRequest(url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + pageToken + "&key=AIzaSyCkJvT6IguUIXVbBAe8-0l2vO1RWbxW4Tk"));
                                        }
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    final FutureTask<String> future = new FutureTask<>(request);

                                    executor.execute(future);
                                    String response = null;

                                    response = future.get();
                                    //Lit le Json du site
                                    jsonPOI = new JSONObject(response);
                                    jsonResults = jsonPOI.getJSONArray("results");

                                    //Enregistre tout les position des POI dans le rayon spécifié
                                    for (int k = 0; k < jsonResults.length(); k++) {


                                        addPoi = true;
                                        tempId = jsonResults.getJSONObject(k).getString("id");
                                        for(int u = 0; u < idPois.size(); u++){
                                            if(tempId.equals(idPois.get(u))){
                                                //Vérifie si le id du nouveau poi est différent des autres pois
                                                addPoi = false;
                                                break;
                                            }
                                        }
                                        if(addPoi){

                                            tempPoi = new Poi(tempId, jsonResults.getJSONObject(k).getString("name"), jsonResults.getJSONObject(k).getJSONArray("types"));

                                            tempPos = new LatLng(jsonResults.getJSONObject(k).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                                    jsonResults.getJSONObject(k).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                                            tempMarker = map.addMarker(new MarkerOptions().flat(false).snippet("POI")
                                                    .position(tempPos).icon(BitmapDescriptorFactory
                                                            .fromResource(R.drawable.coffre)));
                                            //affecte l'objet poi au marqueur pour y avoir acces ensuite
                                            tempMarker.setTag(tempPoi);
                                            idPois.add(tempId);
                                        }

                                        Log.d("Request", "json " + k + " " + nbrPage);
                                    }


                                    pageToken = jsonPOI.getString("next_page_token");
                                    nbrPage++;

                                    //La boucle se realise tant qu'il y a 20item dans la page, ce qui est la quantité maximal de donnés par pages
                                } while (jsonPOI.getJSONArray("results").length() == 20);
                            } catch (JSONException | InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                        poiUpdate = livePos;

                        //Arrete l'animation de chargement
                        loading.setVisibility(View.GONE);
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
        }catch(SecurityException e){
            e.printStackTrace();
        }
        //Réaction au clique sur un marqueur
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                distanceFromPoi = new float[1];
                Location.distanceBetween(livePos.latitude,livePos.longitude,marker.getPosition().latitude,marker.getPosition().longitude, distanceFromPoi);
                actualPoi = (Poi) marker.getTag();
                Toast.makeText(getApplicationContext(),actualPoi.getName(),Toast.LENGTH_SHORT);

                if(marker.getSnippet().equals("POI") && distanceFromPoi[0]<50){
                    //Si le marqueur est un poi et si il est a moins de 50m de l'utilisateur
                    actualPoi = (Poi) marker.getTag();
                    if(Objects.requireNonNull(actualPoi).isActive()){
                        startActivityForResult(new Intent(getApplicationContext(), PopupRecompenses.class),1);
                        SurChangementActivity = true;
                    }else{
                        Toast.makeText(getApplicationContext(),getString(R.string.recompense_dipo_dans) + String.valueOf(actualPoi.getTimeLeft()) + getString(R.string.secondes), Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
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

                Toast.makeText(getApplicationContext(), R.string.jeu_de_jour_desactive, Toast.LENGTH_LONG).show();
            }
        }
    }

    //Désactive le poi si l'utilisateur à récuperer la récompense
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //vérifie la source du retour
        if (requestCode == 1) {
            //vérifie que l'utilisateur à bien récuperé la récompense
            if (resultCode == RESULT_OK) {
                if(data.getStringExtra("RECUPERER").equals("ok")){
                    actualPoi.setTimer(600000);
                    actualPoi.setActive(false);
                }
            }
        }
    }

}
