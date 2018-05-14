package daynight.daynnight;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import static daynight.daynnight.MainActivity.SurChangementActivity;
import static daynight.daynnight.MainActivity.joueur;

/**
 * Created by sebastien on 18-03-17.
 */

public class ChoixBarreDOutils extends Activity
{
    //Variables
    //static Outil objet;

    //Constructeurs
    public ChoixBarreDOutils() {}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choix_barre_doutils);

        //objet = getIntent().getExtras().getParcelable("objet");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        LinearLayout layoutBarreDOutils = (LinearLayout) findViewById(R.id.layout_barreDOutils);
        ViewGroup.LayoutParams params = layoutBarreDOutils.getLayoutParams();

        //Attribuer
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getWindow().setLayout((int) (width), (int) (params.height));
            getWindow().setGravity(Gravity.BOTTOM);
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            getWindow().setLayout((int) (height), (int) (params.height));
        }

        Toast message = Toast.makeText(getApplicationContext(),"Sélectionnez la case où vous désirez déposer votre outil dans la barre d'outils", Toast.LENGTH_LONG);
        message.setGravity(Gravity.CENTER, 0, 0);
        message.show();
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
    protected void onResume()
    {
        MainActivity.musiqueDeFond.start();
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SurChangementActivity = false;
    }

    //Méthodes
    public void startActivity(/*Outil outil,*/Context context)
    {
        Intent intent = new Intent(context, ChoixBarreDOutils.class);
        //intent.putExtra("objet", outil);
        context.startActivity(intent);
    }
}
