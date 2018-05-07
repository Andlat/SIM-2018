package daynight.daynnight;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;

import java.io.File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity
{
    static MainActivity ma;
    static File fichierJoueur = new File("joueurDNN");
    public static Joueur joueur;

    public static boolean onPause = false;
    public static boolean SurChangementActivity = false;

    public static MediaPlayer musiqueDeFond;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ma = this;

        //getApplicationContext().deleteFile(fichierJoueur.getName());
        if(fileExists(getApplicationContext(), fichierJoueur.getName()))
        {
            Scanner actualiser = new Scanner(lireJoueur());
            joueur = new Joueur(actualiser.next(), actualiser.next(), actualiser.next(), actualiser.nextInt(), actualiser.nextInt());
            Log.wtf("CONFIRMATION", joueur.getPrenom() + " " + joueur.getNom());
        }
        else
        {
            startActivity(new Intent(MainActivity.this, PopupNouveauJoueur.class));
        }

        //Musique d'arriere plan
        musiqueDeFond = MediaPlayer.create(MainActivity.this, R.raw.musiquebackground);
        musiqueDeFond.setLooping(true);
        musiqueDeFond.start();

        //Bouton jeu de jour
        Button buttonDay = (Button) findViewById(R.id.jourButton);
        buttonDay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        //Bouton jeu de nuit
        Button buttonNight = (Button) findViewById(R.id.nuitButton);
        buttonNight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        //Bouton pour l'inventaire
        Button leSebBouton = (Button) findViewById(R.id.leSebBouton);
        leSebBouton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, Inventaire.class);
                startActivity(intent);
            }
        });

        //Bouton pour les badges
        Button leSebNouton2 = (Button) findViewById(R.id.leSebBouton2);
        leSebNouton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, ListeBadges.class);
                startActivity(intent);
            }
        });

        //Bouton pour réglages
        Button boutonReglages = (Button) findViewById(R.id.boutonReglages);
        boutonReglages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        onPause = true;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        musiqueDeFond.start();
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        if(onPause && !SurChangementActivity)
        {
            sauvegardeJoueur(joueur);
            musiqueDeFond.pause();
            onPause = false;
        }
    }
    @Override
    public void finish()
    {
        super.finish();
    }

    //Méthodes
    public boolean fileExists(Context context, String filename)
    {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists())
        {
            return false;
        }
        return true;
    }
    public void sauvegardeJoueur(Joueur joueur)
    {
        if(fileExists(getApplicationContext(), fichierJoueur.getName()))
        {
            //Lire le joueur sauvegardé
            String sauvegarDeJoueur = lireJoueur();
            if(sauvegarDeJoueur == null)
                sauvegarDeJoueur = "";


            //nouvelle souvegarde du joueur en string
            String nouvJoueur = joueur.getPrenom() + " " + joueur.getNom() + " " + joueur.getAdresseElectronique() + " " + joueur.getSkin() + " " + joueur.getBiscuits(); //TODO À mettre les autres

            //Écrire dans le fichier
            FileOutputStream enregistrer = null;

            try
            {
                enregistrer = openFileOutput(fichierJoueur.getName(), Context.MODE_PRIVATE);
                enregistrer.write(nouvJoueur.getBytes());

                Log.wtf("J sauvegardé MAINTENANT ", nouvJoueur);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    enregistrer.close();//Fermer le fichier
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public String lireJoueur()
    {
        FileInputStream joueur = null;

        try
        {
            joueur = openFileInput(fichierJoueur.getName());
            byte[] buffer = new byte[1];
            StringBuilder sauvegarDeJoueur = new StringBuilder();

            while((joueur.read(buffer)) != -1)
            {
                sauvegarDeJoueur.append(new String(buffer));
            }
            Log.wtf("Fichier ", "Lecture de " + fichierJoueur.getName() + " réussi");
            Log.wtf("J sauvergardé ", sauvegarDeJoueur.toString());
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
