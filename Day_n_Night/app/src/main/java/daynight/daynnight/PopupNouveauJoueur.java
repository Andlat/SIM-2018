package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static daynight.daynnight.MainActivity.SurChangementActivity;
import static daynight.daynnight.MainActivity.fichierJoueur;
import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.onPause;

/**
 * Created by sebastien on 18-03-17.
 */

public class PopupNouveauJoueur extends Activity
{
    //Variables
    Button confirmer;


    //Constructeurs
    public PopupNouveauJoueur() {}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_nouveau_joueur);

        confirmer = (Button) findViewById(R.id.confirmation);

        confirmer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText prenom = findViewById(R.id.prenom);
                EditText nom = findViewById(R.id.nom);
                EditText adresseElectronique = findViewById(R.id.adresseElectronique);

                if(!Objects.equals(prenom.getText().toString().replaceAll(" ", ""), "") || !Objects.equals(nom.getText().toString().replaceAll(" ", ""), "") || !Objects.equals(adresseElectronique.getText().toString().replaceAll(" ", ""), ""))
                {
                    MainActivity.joueur = new Joueur(prenom.getText().toString(), nom.getText().toString(), adresseElectronique.getText().toString(), getApplicationContext());
                    try
                    {
                        openFileOutput(fichierJoueur.getName(), Context.MODE_PRIVATE).close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    finish();
                    SurChangementActivity = false;
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Le prénom, le nom ou l'adresse électronique ne sont pas valide...", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        //Attribuer
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getWindow().setLayout((int) (width * 0.85), (int) (height * 0.40));
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            getWindow().setLayout((int) (height * 0.85), (int) (width * 0.40));
        }
    }
    @Override
    protected void onStop()
    {
        if(SurChangementActivity)
        {
            /*MainActivity.musiqueDeFond.pause();
            MainActivity.ma.sauvegardeJoueur(joueur);*/
        }
        super.onStop();
    }
    @Override
    protected void onResume()
    {
        //MainActivity.musiqueDeFond.start();
        super.onResume();
    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }
    //Getteurs & setteurs
}
