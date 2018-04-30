package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static daynight.daynnight.MainActivity.joueur;

/**
 * Created by sebastien on 18-03-17.
 */

public class PopupConnexion extends Activity
{
    //Variables



    //Constructeurs
    public PopupConnexion() {}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_connexion);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        //Attribuer
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getWindow().setLayout((int) (width * 0.85), (int) (height * 0.30));
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            getWindow().setLayout((int) (height * 0.85), (int) (width * 0.30));
        }
    }

    @Override
    protected void onStop()
    {
        MainActivity.ma.sauvegardeJoueur(joueur);
        MainActivity.musiqueDeFond.pause();
        super.onStop();
    }
    @Override
    protected void onResume()
    {
        MainActivity.musiqueDeFond.start();
        super.onResume();
    }
    //Getteurs & setteurs

    //Méthodes

}
