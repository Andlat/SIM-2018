package daynight.daynnight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCALISATION_REQUEST = 1;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCALISATION_REQUEST);
            }
        }




    }



    @Override
    public void onMapReady(GoogleMap googleMap) {


    }



    //Gère les réponses des demandes de permissions
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == LOCALISATION_REQUEST) {
            if (permissions.length != 1
                    || !Objects.equals(permissions[0], Manifest.permission.ACCESS_FINE_LOCATION)
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "Le jeu de jour a été désactivé.\nActivez la géolocalisation dansles réglages pour l'activer!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
