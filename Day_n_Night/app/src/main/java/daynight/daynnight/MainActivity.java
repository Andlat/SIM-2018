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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import daynight.daynnight.Objet.Type;

import static daynight.daynnight.Objet.Type.Décoration;
import static daynight.daynnight.Objet.Type.Outil;
import static daynight.daynnight.Objet.Type.Skin;

public class MainActivity extends AppCompatActivity
{
    static MainActivity ma;
    static File fichierJoueur = new File("joueurDNN");
    static File fichierOutilsInventaire = new File("outilsInventaireDNN");
    static File fichierSkinsInventaire = new File("skinsInventaireDNN");
    static File fichierDecorationsInventaire = new File("decorationsInventaireDNN");
    static File fichierOutilsBoutique = new File("outilsBoutiqueDNN");
    static File fichierSkinsBoutique = new File("skinsBoutiqueDNN");
    static File fichierDecorationsBoutique = new File("decorationsBoutiqueDNN");
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

        /*getApplicationContext().deleteFile(fichierJoueur.getName());
        getApplicationContext().deleteFile(fichierSkinsInventaire.getName());
        getApplicationContext().deleteFile(fichierSkinsBoutique.getName());
        getApplicationContext().deleteFile(fichierOutilsBoutique.getName());
        getApplicationContext().deleteFile(fichierOutilsInventaire.getName());*/
        if(fileExists(getApplicationContext(), fichierJoueur.getName()))
        {
            Scanner actualiser;
            try
            {
                actualiser = new Scanner(lireJoueur());
                joueur = new Joueur(actualiser.next(), actualiser.next(), actualiser.next(), actualiser.nextInt(), actualiser.nextInt(), getApplicationContext());
                Log.wtf("CONFIRMATION", joueur.getPrenom() + " " + joueur.getNom());
            }
            catch (NoSuchElementException e)
            {
                e.printStackTrace();
                startActivity(new Intent(MainActivity.this, PopupNouveauJoueur.class));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
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
            //nouvelle souvegarde du joueur en string
            String nouvJoueur = joueur.getPrenom() + " " + joueur.getNom() + " " + joueur.getAdresseElectronique() + " " + joueur.getSkin() + " " + joueur.getBiscuits(); //TODO À mettre les autres
            String nouvOutilsInventaire = "";
            for(int i = 0 ; i < joueur.getOutilsInventaire().size() ; i++)
            {
                nouvOutilsInventaire = nouvOutilsInventaire + joueur.getOutilsInventaire().get(i).getId() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getNom() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getDescription() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getType() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getRarete() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getPortee() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getPrix() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getToucherParCoup() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getNbCibles() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getIntervalleParCoup() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getImageDrawableString() + System.lineSeparator() + joueur.getOutilsInventaire().get(i).getAcquis() + System.lineSeparator();
            }
            String nouvSkinsInventaire = "";
            for(int i = 0 ; i < joueur.getSkinsInventaire().size() ; i++)
            {
                nouvSkinsInventaire = nouvSkinsInventaire + joueur.getSkinsInventaire().get(i).getId() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getNom() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getDescription() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getType() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getRarete() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getPortee() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getPrix() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getToucherParCoup() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getNbCibles() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getIntervalleParCoup() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getImageDrawableString() + System.lineSeparator() + joueur.getSkinsInventaire().get(i).getAcquis() + System.lineSeparator();
            }
            String nouvOutilsBoutique = "";
            for(int i = 0 ; i < joueur.getOutilsBoutique().size() ; i++)
            {
                nouvOutilsBoutique = nouvOutilsBoutique + joueur.getOutilsBoutique().get(i).getId() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getNom() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getDescription() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getType() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getRarete() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getPortee() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getPrix() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getToucherParCoup() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getNbCibles() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getIntervalleParCoup() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getImageDrawableString() + System.lineSeparator() + joueur.getOutilsBoutique().get(i).getAcquis() + System.lineSeparator();
            }
            String nouvSkinsBoutique = "";
            for(int i = 0 ; i < joueur.getSkinsBoutique().size() ; i++)
            {
                nouvSkinsBoutique = nouvSkinsBoutique + joueur.getSkinsBoutique().get(i).getId() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getNom() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getDescription() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getType() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getRarete() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getPortee() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getPrix() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getToucherParCoup() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getNbCibles() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getIntervalleParCoup() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getImageDrawableString() + System.lineSeparator() + joueur.getSkinsBoutique().get(i).getAcquis() + System.lineSeparator();
            }

            //Écrire dans le fichier
            FileOutputStream enregistrerJoueur = null;

            FileOutputStream enregistrerOutilsInventaire = null;
            FileOutputStream enregistrerSkinsInventaire = null;
            //FileOutputStream enregistrerDecorationsInventaire = null;
            FileOutputStream enregistrerOutilsBoutique = null;
            FileOutputStream enregistrerSkinsBoutique = null;
            //FileOutputStream enregistrerDecorationsBoutique = null;

            try
            {
                enregistrerJoueur = openFileOutput(fichierJoueur.getName(), Context.MODE_PRIVATE);
                enregistrerJoueur.write(nouvJoueur.getBytes());
                Log.wtf("J sauvegardé MAINTENANT ", nouvJoueur);

                enregistrerOutilsInventaire = openFileOutput(fichierOutilsInventaire.getName(), Context.MODE_PRIVATE);
                enregistrerOutilsInventaire.write(nouvOutilsInventaire.getBytes());
                Log.wtf("OI sauvegardé MAINTENANT ", nouvOutilsInventaire);

                enregistrerSkinsInventaire = openFileOutput(fichierSkinsInventaire.getName(), Context.MODE_PRIVATE);
                enregistrerSkinsInventaire.write(nouvSkinsInventaire.getBytes());
                Log.wtf("SI sauvegardé MAINTENANT ", nouvSkinsInventaire);

                enregistrerOutilsBoutique = openFileOutput(fichierOutilsBoutique.getName(), Context.MODE_PRIVATE);
                enregistrerOutilsBoutique.write(nouvOutilsBoutique.getBytes());
                Log.wtf("OB sauvegardé MAINTENANT ", nouvOutilsBoutique);

                enregistrerSkinsBoutique = openFileOutput(fichierSkinsBoutique.getName(), Context.MODE_PRIVATE);
                enregistrerSkinsBoutique.write(nouvSkinsBoutique.getBytes());
                Log.wtf("SB sauvegardé MAINTENANT ", nouvSkinsBoutique);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    enregistrerJoueur.close();//Fermer le fichier
                    enregistrerOutilsInventaire.close();
                    enregistrerSkinsInventaire.close();
                    enregistrerOutilsBoutique.close();
                    enregistrerSkinsBoutique.close();
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
