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


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

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
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        move = new float[1];
        prevPos = new LatLng(0, 0);

        animationDrawable.addFrame(getResources().getDrawable(R.drawable.arthur1_1), 300);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.arthur1_2), 300);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.arthur1_3), 300);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.arthur1_4), 300);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.arthur1_5), 300);

        boutonCenter = (findViewById(R.id.boutonCenter));
        boutonCenter.setClickable(false);
        boutonCenter.setVisibility(View.INVISIBLE);
        boutonCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOCALISATION_UPDATE = true;
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


        try {
            //Actulise la position sur la carte à chaque x ms
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    livePos = new LatLng(location.getLatitude(), location.getLongitude());

                    final View persomarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

                    //Distance entre la position actuelle et la dernière actualisation
                    Location.distanceBetween(prevPos.latitude, prevPos.longitude, livePos.latitude, livePos.longitude, move);
                    Log.d("Move", String.valueOf(move[0]));

                    //Si la distance entre deux actualisation est suppérieur à 3m alors le personnage se déplace
                    if (move[0] > 3) {

                        if (perso != null) perso.remove();

                        /*perso = map.addMarker(new MarkerOptions()
                                .position(livePos)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icondude)));*/
                        //ImageView imageView = new ImageView(null);
                        //imageView.setBackgroundResource(R.drawable.mapcharacteranimation1);

                        perso = map.addMarker(new MarkerOptions()
                                .position(livePos)
                                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getApplicationContext(), persomarker))));

                        //map.moveCamera(CameraUpdateFactory.newLatLng(livePos));/**/

                        if (LOCALISATION_UPDATE == true) {
                            map.animateCamera(CameraUpdateFactory.newLatLng(livePos), (int) move[0] / 5, null);
                        }

                        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                            @Override
                            public void onCameraMoveStarted(int i) {
                                LOCALISATION_UPDATE = false;
                                map.stopAnimation();
                                boutonCenter.setClickable(true);
                                boutonCenter.setVisibility(View.VISIBLE);
                            }
                        });

                        persomarker.startAnimation(animationDrawable);
                        //mapCharacterAnimation.start();

                        new CountDownTimer(5000,5000){
                            @Override
                            public void onTick(long l){}
                            public void onFinish(){
                                persomarker.clearAnimation();
                            }
                        };
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

        //Réaction au clique d'un POI
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                float[] distancePOI = new float[1];

                Location.distanceBetween(livePos.latitude, livePos.longitude, poi.latLng.latitude, poi.latLng.longitude, distancePOI);
                Log.d("Distance", String.valueOf(distancePOI[0]));
                if(distancePOI[0] < 100){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    LayoutInflater inflater = MapActivity.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.reward_dialog, null));
                    builder.show();
                }
            }
        });
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
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
