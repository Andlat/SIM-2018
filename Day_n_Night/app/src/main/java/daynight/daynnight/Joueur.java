package daynight.daynnight;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static daynight.daynnight.Objet.Type.Skin;
import static daynight.daynnight.Outil.Portee.Nulle;

/**
 * Created by sebastien on 18-03-28.
 */

public class Joueur
{
    //Propriétés
    String prenom;
    String nom;
    String adresseElectronique;
    int skin; //Drawable
    private int bestScore;

    int biscuits;
    List<ArrayList<Outil>> inventaire;
    Outil[] barreDOutils = new Outil[5];
    Outil outilSelection = null;
    List<ArrayList<Outil>> boutique;
    List<Badge> badges;

    Context context;

    //Préférences
    Boolean musique;
    String langue;

    //Statistiques
    int NbreVaguesAtteintes;


    //Constructeurs
    Joueur() {}
    //Constructeur appelé lors de l'actualisation des données .txt du joueur actuel //TODO à finir
    Joueur(String prenom, String nom, String addresseElectronique, int skin, int biscuits, Boolean musique, String langue, Context context) throws FileNotFoundException
    {
        this.prenom = prenom;
        this.nom = nom;
        this.adresseElectronique = addresseElectronique;
        this.skin = skin;
        this.biscuits = biscuits;
        this.bestScore = 0;

        this.musique = musique;
        this.langue = langue;

        this.NbreVaguesAtteintes = 0;

        boutique = new ArrayList<>(3);
        inventaire = new ArrayList<>(3);
        for(int i = 0 ; i < 5 ; i++)
            barreDOutils[i] = new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0,0f, "", true);

        this.context = context;

        ArrayList<Outil> decorationsBout = new ArrayList<>();
        ArrayList<Outil> decorationsInv = new ArrayList<>();
        //Ajout de cases vides
        for(int j = 0 ; j < 60 ; j++)
        {
            decorationsBout.add(new Outil(666,"Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Nulle, 0, 0, 0,0f, "", true));
        }

        boutique.add(lireOutils(context.openFileInput(MainActivity.fichierOutilsBoutique.getName())));
        boutique.add(lireSkins(context.openFileInput(MainActivity.fichierSkinsBoutique.getName())));
        boutique.add(decorationsBout);
        inventaire.add(lireOutils(context.openFileInput(MainActivity.fichierOutilsInventaire.getName())));
        inventaire.add(lireSkins(context.openFileInput(MainActivity.fichierSkinsInventaire.getName())));
        inventaire.add(decorationsInv);
    }
    //Constructeur appelé lors de la création du joueur
    Joueur(String prenom, String nom, String adresseElectronique, Context context)
    {
        this.prenom = prenom;
        this.nom = nom;
        this.adresseElectronique = adresseElectronique;
        this.skin = R.drawable.arthur1_1;
        this.biscuits = 44;
        this.musique = true;
        this.langue = "fr";

        boutique = new ArrayList<>(3);
        inventaire = new ArrayList<>(3);
        for(int i = 0 ; i < 5 ; i++)
            barreDOutils[i] = new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0,0f, "", true);

        this.context = context;

        ArrayList<Outil> decorationsBout = new ArrayList<>();
        ArrayList<Outil> outilsInv = new ArrayList<>();
        ArrayList<Outil> skinsInv = new ArrayList<>();
        skinsInv.add(new Outil(1, "Le Classique", "Votre chemise et chandail vous vont déjà à merveille. Pourquoi voudriez vous changer une recette gagnante?", Skin, 1, Nulle, 0, 0, 0, 0f, "arthur1_1", true));
        ArrayList<Outil> decorationsInv = new ArrayList<>();
        //Ajout de cases vides
        for(int j = 0 ; j < 60 ; j++)
        {
            decorationsBout.add(new Outil(666,"Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Nulle, 0, 0, 0,0f, "", true));
        }

        boutique.add(lireOutils(context.getResources().openRawResource(R.raw.outils_depart)));
        boutique.add(lireSkins(context.getResources().openRawResource(R.raw.skins_depart)));
        boutique.add(decorationsBout);
        inventaire.add(outilsInv);
        inventaire.add(skinsInv);
        inventaire.add(decorationsInv);
    }

    //Getteurs & Setteurs
    public String getPrenom()
    {
        return prenom;
    }
    public String getNom()
    {
        return nom;
    }
    public String getAdresseElectronique()
    {
        return adresseElectronique;
    }
    public int getSkin()
    {
        return skin;
    }
    public int getBiscuits()
    {
        return biscuits;
    }
    public List<ArrayList<Outil>> getInventaire() {
        return inventaire;
    }
    public ArrayList<Outil> getOutilsInventaire()
    {
        return inventaire.get(0);
    }
    public ArrayList<Outil> getSkinsInventaire()
    {
        return inventaire.get(1);
    }
    public ArrayList<Outil> getDecorationsInventaire()
    {
        return inventaire.get(2);
    }
    public Outil[] getBarreDOutils()
    {
        return barreDOutils;
    }
    public Outil getOutilSelection()
    {
        return outilSelection;
    }
    public List<ArrayList<Outil>> getBoutique() {
        return boutique;
    }
    public ArrayList<Outil> getOutilsBoutique()
    {
        return boutique.get(0);
    }
    public ArrayList<Outil> getSkinsBoutique()
    {
        return boutique.get(1);
    }
    public ArrayList<Outil> getDecorationsBoutique()
    {
        return boutique.get(2);
    }
    public Boolean getMusique() {
        return musique;
    }
    public String getLangue() {
        return langue;
    }

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }
    public void setNom(String nom)
    {
        this.nom = nom;
    }
    public void setAddresseElectronique(String addresseElectronique)
    {
        this.adresseElectronique = addresseElectronique;
    }
    public void setSkin(int skin)
    {
        this.skin = skin;
    }
    public void setBiscuits(int biscuits)
    {
        this.biscuits = biscuits;
    }
    public void setInventaire(List<ArrayList<Outil>> inventaire) {
        this.inventaire = inventaire;
    }
    public void setOutilsInventaire(ArrayList<Outil> outils)
    {
        this.inventaire.set(0, outils);
    }
    public void setSkinsInventaire(ArrayList<Outil> skins)
    {
        this.inventaire.set(1, skins);
    }
    public void setDecorationsInventaire(ArrayList<Outil> decorations)
    {
        this.inventaire.set(2, decorations);
    }
    public void setBarreDOutils(Outil[] barreDOutils)
    {
        this.barreDOutils = barreDOutils;
    }
    public void setOutilSelection(Outil outilSelection)
    {
        this.outilSelection = outilSelection;
    }
    public void setBoutique(List<ArrayList<Outil>> boutique) {
        this.boutique = boutique;
    }
    public void setOutilsBoutique(ArrayList<Outil> outils)
    {
        this.boutique.set(0, outils);
    }
    public void setSkinsBoutique(ArrayList<Outil> skins)
    {
        this.boutique.set(1, skins);
    }
    public void setDecorationsBoutique(ArrayList<Outil> decorations)
    {
        this.boutique.set(2, decorations);
    }
    public void setMusique(Boolean musique) {
        this.musique = musique;
    }
    public void setLangue(String langue) {
        this.langue = langue;
    }

    //Méthodes
    public ArrayList<Outil> lireOutils(InputStream inputStream)
    {
        ArrayList<Outil> outils = new ArrayList<Outil>();
        try
        {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader buffReader = new BufferedReader(isr);
            String line;
            StringBuilder text = new StringBuilder();

            int id;
            String nom;
            String description;
            String type;
            int rarete;
            String portee;
            int prix;
            int toucherParCoup;
            int nbCibles;
            float intervalleParCoup;
            String imageDrawableString;
            Boolean acquis;

            while (( line = buffReader.readLine()) != null)
            {
                //Lire chaque ligne et donner les valeurs
                text.setLength(0);
                text.append(line);
                id = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                nom = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                description = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                type = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                rarete = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                portee = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                prix = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                toucherParCoup = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                nbCibles = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                intervalleParCoup = Float.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                imageDrawableString = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                acquis = Boolean.valueOf(text.toString());

                outils.add(new Outil(id, nom, description, Objet.Type.Outil, rarete, Outil.Portee.valueOf(portee), prix, toucherParCoup, nbCibles, intervalleParCoup, imageDrawableString, acquis));
            }
        } catch (IOException e)
        {
            return null;
        }

        return outils;
    }
    public ArrayList<Outil> lireSkins(InputStream inputStream)
    {
        ArrayList<Outil> skins = new ArrayList<Outil>();
        try
        {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader buffReader = new BufferedReader(isr);
            String line;
            StringBuilder text = new StringBuilder();

            int id;
            String nom;
            String description;
            String type;
            int rarete;
            String portee;
            int prix;
            int toucherParCoup;
            int nbCibles;
            float intervalleParCoup;
            String imageDrawableString;
            Boolean acquis;

            while (( line = buffReader.readLine()) != null)
            {
                //Lire chaque ligne et donner les valeurs
                text.setLength(0);
                text.append(line);
                id = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                nom = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                description = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                type = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                rarete = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                portee = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                prix = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                toucherParCoup = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                nbCibles = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                intervalleParCoup = Float.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                imageDrawableString = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                acquis = Boolean.valueOf(text.toString());

                skins.add(new Outil(id, nom, description, Skin, rarete, Nulle, prix, 0, 0, 0f, imageDrawableString, acquis));
            }
        } catch (IOException e)
        {
            return null;
        }

        return skins;
    }
    //Change la langue de l'application
    //Fonction de Rubin Nellikunnathu sur https://stackoverflow.com/questions/34573201/change-languages-in-app-via-strings-xml
    public void setLocale(Context context)
    {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(this.langue);
        res.updateConfiguration(conf, dm);
    }
}
