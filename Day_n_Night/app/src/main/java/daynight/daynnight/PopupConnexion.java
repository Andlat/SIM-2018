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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static daynight.daynnight.MainActivity.fichierJoueur;
import static daynight.daynnight.MainActivity.joueur;

/**
 * Created by sebastien on 18-03-17.
 */

public class PopupConnexion extends Activity
{
    //Variables
    String prenom, nom;
    Button confirmer, commencerPartie;


    //Constructeurs
    public PopupConnexion() {}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_connexion);

        confirmer = (Button) findViewById(R.id.confirmation);
        commencerPartie = (Button) findViewById(R.id.commencerPartie);

        confirmer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText txt = findViewById(R.id.prenom);
                EditText txt2 = findViewById(R.id.nom);

                prenom = txt.getText().toString();
                nom = txt2.getText().toString();
                try
                {
                    MainActivity.connexion = MainActivity.joueur.connexion(getApplicationContext(), prenom, nom, lireJoueur());//Amorcer la connexion
                    if(MainActivity.connexion)
                    {
                        finish();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        commencerPartie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });


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
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }
    //Getteurs & setteurs

    //Méthodes
    public String lireJoueur()
    {
        FileInputStream joueur = null;


        if(!fichierJoueur.isFile() && !fichierJoueur.canRead())
        {
            Joueur j = new Joueur();
            String nouvJoueur = j.getPrenom().replaceAll(" ", "&") + " " + j.getNom().replaceAll(" ", "&") + " " + j.getAddresseElectronique() + " " + j.getBiscuits(); //TODO À mettre les autres
            FileOutputStream enregistrer = null;
            try
            {
                enregistrer = openFileOutput(fichierJoueur.getName(), Context.MODE_PRIVATE);
                enregistrer.write(nouvJoueur.getBytes());

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (enregistrer != null)
                    {
                        enregistrer.close();//Fermer le fichier
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        try
        {
            joueur = openFileInput(fichierJoueur.getName());
            byte[] buffer = new byte[1];
            StringBuilder sauvegarDeJoueur = new StringBuilder();

            while((joueur.read(buffer)) != -1)
            {
                sauvegarDeJoueur.append(new String(buffer));
            }
            Log.wtf("Fichier :", "Lecture de " + joueur.toString() + " réussi");
            Log.wtf("J sauvergardé :", sauvegarDeJoueur.toString());
            return sauvegarDeJoueur.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if(joueur != null)
            {
                try
                {
                    joueur.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
