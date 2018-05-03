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

        //getIntent().setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        setContentView(R.layout.activity_main);
        ma = this;


        //getApplicationContext().deleteFile(fichierJoueur.getName());
        if(fileExists(getApplicationContext(), fichierJoueur.getName()))
        {
            Scanner actualiser = new Scanner(lireJoueur());
            joueur = new Joueur(actualiser.next(), actualiser.next(), actualiser.next(), actualiser.nextInt());
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
                //Ceci est juste un test pour le bouton pause
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
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
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
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
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
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
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
                startActivity(intent);
            }
        });
    }

    //Arrête la musique lorsque l'application ferme et commence quand elle ouvre
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
            musiqueDeFond.pause();
            sauvegardeJoueur(joueur);
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
            String nouvJoueur = joueur.getPrenom() + " " + joueur.getNom() + " " + joueur.getAdresseElectronique() + " " + joueur.getBiscuits(); //TODO À mettre les autres

            //Écrire dans le fichier
            FileOutputStream enregistrer = null;

            try
            {
                enregistrer = openFileOutput(fichierJoueur.getName(), Context.MODE_PRIVATE);
                enregistrer.write(nouvJoueur.getBytes());

                Log.wtf("J sauvegardé MAINTENANT :", nouvJoueur);
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
    public void actualiserJoueur(Joueur joueur)
    {
        //Lire le joueur sauvegardé
        String sauvegarDeJoueur = lireJoueur();
        if(sauvegarDeJoueur == null)
            sauvegarDeJoueur = "";

        Scanner verifier = new Scanner(sauvegarDeJoueur).useLocale(Locale.US);

        try
        {
            //joueur = new Joueur(verifier.next().replaceAll("&", " "), verifier.next().replaceAll("&", " "), verifier.next(), verifier.nextInt());
            Log.wtf("J actualisé :", joueur.getPrenom() + " " + joueur.getNom() + " " + joueur.getAdresseElectronique() + " " + joueur.getBiscuits());

        }
        catch(NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }
    public String lireJoueur()
    {
        FileInputStream joueur = null;


        /*if(!fichierJoueur.isFile() && !fichierJoueur.canRead())
        {
            /*Joueur j = new Joueur();
            String nouvJoueur = j.getPrenom().replaceAll(" ", "&") + " " + j.getNom().replaceAll(" ", "&") + " " + j.getAddresseElectronique() + " " + j.getBiscuits(); //TODO À mettre les autres
            FileOutputStream enregistrer = null;
            try
            {
                enregistrer = openFileOutput(fichierJoueur.getName(), Context.MODE_PRIVATE);
                //enregistrer.write(nouvJoueur.getBytes());
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
        }*/

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
    /*public void supprimerJoueur(Joueur joueur)
    {
        String payeurASupprimer = payeur.getIdentifiant() + " " + payeur.getId() + " " + payeur.getMontant();
        StringBuilder nouvListePayeurs = new StringBuilder();

        //Lire comptes présents
        String listePayeurs = lireJoueur();
        if(listePayeurs == null)
            listePayeurs = "";

        //Écrire dans le fichier
        FileOutputStream enregistrer = null;

        try
        {
            BufferedReader br = new BufferedReader(new StringReader(listePayeurs));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (!line.trim().equals(payeurASupprimer))
                {
                    nouvListePayeurs.append(line).append(System.lineSeparator());
                }
            }
            br.close();

            enregistrer = openFileOutput("payeurs", Context.MODE_PRIVATE);
            enregistrer.write(nouvListePayeurs.toString().getBytes());
            //System.out.println("Liste de payeurs de base :" + listePayeurs);
            //System.out.println("Payeur supprimé :" + payeurASupprimer);
            //System.out.println("Écriture réussie");
            //System.out.println("Écriture :" + nouvListePayeurs);
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
    }*/
}
