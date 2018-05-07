package daynight.daynnight;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

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

    int biscuits;
    List<ArrayList<Outil>> inventaire;
    List<ArrayList<Outil>> boutique;
    List<Badge> badges;

    //TODO Je comprend pas ce que seb a fait donc je fait ca
    ArrayList<Outil> items;
    Context context;

    //Préférences
    Boolean musique;


    //Constructeurs
    Joueur() {}
    //Constructeur appelé lors de l'actualisation des données .txt du joueur actuel //TODO à finir
    Joueur(String prenom, String nom, String addresseElectronique, int skin, int biscuits)
    {
        this.prenom = prenom;
        this.nom = nom;
        this.adresseElectronique = addresseElectronique;
        this.skin = skin;
        this.biscuits = biscuits;


        //POUR L'INSTANT
        boutique = new ArrayList<>(3);
        inventaire = new ArrayList<>(3);

        ArrayList<Outil> outilsBout = new ArrayList<>();
        ArrayList<Outil> skinsBout = new ArrayList<>();
        ArrayList<Outil> decorationsBout = new ArrayList<>();
        ArrayList<Outil> outilsInv = new ArrayList<>();
        ArrayList<Outil> skinsInv = new ArrayList<>();
        ArrayList<Outil> decorationsInv = new ArrayList<>();

        //Ajout de cases vides
        for(int j = 0 ; j < 56 ; j++)
        {
            outilsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            skinsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            decorationsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));

            outilsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            skinsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            decorationsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
        }

        outilsBout.set(0, new Outil("Seau d'eau","Le seau d'eau ne contient pas de l'eau, mais plutôt de la Vodka", Objet.Type.Outil,0, Outil.Portee.Eloignee,6,1,1,"objet_outil_seau_deau", false));
        outilsBout.set(1, new Outil("Master-Ball","La Master-Ball est une Poké-Ball utilisée par les meilleurs dresseurs de pokémons dans Pokémons, il faut être un maitre dans l'art pour l'utiliser!", Objet.Type.Outil,0, Outil.Portee.Eloignee,20,3,1,"objet_outil_masterball", false));
        skinsBout.set(0, new Outil("Pijama","Un pijama rend nos nuits beaucoup plus conforatbles, n'est-ce pas ?", Objet.Type.Skin,0, Outil.Portee.Nulle, 20, 0, 0, "arthur2_1", false));
        skinsBout.set(1, new Outil("Superman","Avec des super pouvoirs aussi puissants que les miens, moi, SuperArthur, je serai inéffrayable!", Objet.Type.Skin, 0, Outil.Portee.Nulle, 40, 0, 0, "arthur7_1", false));

        boutique.add(outilsBout);
        boutique.add(skinsBout);
        boutique.add(decorationsBout);
        inventaire.add(outilsInv);
        inventaire.add(skinsInv);
        inventaire.add(decorationsInv);

        this.context = context;
        //items = readItemFile();
    }
    //Constructeur appelé lors de la création du joueur
    Joueur(String prenom, String nom, String adresseElectronique, Context context)
    {
        this.prenom = prenom;
        this.nom = nom;
        this.adresseElectronique = adresseElectronique;
        this.skin = R.drawable.arthur1_1;
        this.biscuits = 30;

        boutique = new ArrayList<>(3);
        inventaire = new ArrayList<>(3);

        ArrayList<Outil> outilsBout = new ArrayList<>();
        ArrayList<Outil> skinsBout = new ArrayList<>();
        ArrayList<Outil> decorationsBout = new ArrayList<>();
        ArrayList<Outil> outilsInv = new ArrayList<>();
        ArrayList<Outil> skinsInv = new ArrayList<>();
        ArrayList<Outil> decorationsInv = new ArrayList<>();

        //Ajout de cases vides
        for(int j = 0 ; j < 56 ; j++)
        {
            outilsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            skinsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            decorationsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));

            outilsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            skinsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
            decorationsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0, "", true));
        }

        outilsBout.set(0, new Outil("Seau d'eau","Le seau d'eau ne contient pas de l'eau, mais plutôt de la Vodka", Objet.Type.Outil,0, Outil.Portee.Eloignee,6,1,1,"objet_outil_seau_deau", false));
        outilsBout.set(1, new Outil("Master-Ball","La Master-Ball est une Poké-Ball utilisée par les meilleurs dresseurs de pokémons dans Pokémons, il faut être un maitre dans l'art pour l'utiliser!", Objet.Type.Outil,0, Outil.Portee.Eloignee,20,3,1,"objet_outil_masterball", false));
        skinsBout.set(0, new Outil("Pijama","Un pijama rend nos nuits beaucoup plus conforatbles, n'est-ce pas ?", Objet.Type.Skin,0, Outil.Portee.Nulle, 20, 0, 0, "arthur2_1", false));
        skinsBout.set(1, new Outil("Superman","Avec des super pouvoirs aussi puissants que les miens, moi, SuperArthur, je serai inéffrayable!", Objet.Type.Skin, 0, Outil.Portee.Nulle, 40, 0, 0, "arthur7_1", false));

        boutique.add(outilsBout);
        boutique.add(skinsBout);
        boutique.add(decorationsBout);
        inventaire.add(outilsInv);
        inventaire.add(skinsInv);
        inventaire.add(decorationsInv);

        this.context = context;
        //items = readItemFile();
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

    //Méthodes
    private ArrayList<Outil> readItemFile(){
        ArrayList<Outil> outils = new ArrayList<Outil>();
        try{
            InputStream inputStream = context.getResources().openRawResource(R.raw.objets);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader buffReader = new BufferedReader(isr);
            String line;
            StringBuilder text = new StringBuilder();

            int id;
            String drwable;
            String nom;
            int touche;
            float frequence;
            int nbCible;
            float puissance;
            int rarete;
            String porte;
            int prix;
            String description;

            while (( line = buffReader.readLine()) != null) {


                //Lire chaque ligne et donner les valeurs
                text.setLength(0);
                text.append(line);
                id = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                drwable = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                nom = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                touche = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                frequence = Float.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                nbCible = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                puissance = Float.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                rarete = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                porte = text.toString();

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                prix = Integer.valueOf(text.toString());

                line = buffReader.readLine();
                text.setLength(0);
                text.append(line);
                description = text.toString();

                //items.add(new Objet());
            }

        } catch (IOException e) {
            return null;
        }

        return outils;
    }
}
