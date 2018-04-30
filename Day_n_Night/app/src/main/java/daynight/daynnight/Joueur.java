package daynight.daynnight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastien on 18-03-28.
 */

public class Joueur
{
    //Propriétés
    String prenom;
    String nom;
    String addresseElectronique;

    int biscuits;
    List<ArrayList<Outil>> inventaire;
    List<ArrayList<Outil>> boutique;
    List<Badge> badges;

    //Préférences
    Boolean musique;


    //Constructeurs
    Joueur()
    {
        this.prenom = "Arthur";
        this.nom = "ca&rie,&Sarry&pu";//wow
        this.addresseElectronique = "baguettefrancaise@hotmail.com";

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
            outilsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, "", true));
            skinsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, "", true));
            decorationsBout.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, "", true));

            outilsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, "", true));
            skinsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, "", true));
            decorationsInv.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, "", true));
        }

        outilsBout.set(0, new Outil("Seau d'eau","Le seau d'eau ne contient pas de l'eau, mais plutôt de la Vodka", Objet.Type.Outil, Outil.Portee.Éloignée,6,1,1,"objet_outil_seau_deau", false));
        outilsBout.set(1, new Outil("Master-Ball","La Master-Ball est une Poké-Ball utilisée par les meilleurs dresseurs de pokémons dans Pokémons, il faut être un maitre dans l'art pour l'utiliser!", Objet.Type.Outil, Outil.Portee.Éloignée,20,3,1,"objet_outil_masterball", false));
        skinsBout.set(0, new Outil("Pijama","Un pijama rend nos nuits beaucoup plus conforatbles, n'est-ce pas ?", Objet.Type.Skin, Outil.Portee.Nulle, 20, 0, 0, "arthur2_1", false));
        skinsBout.set(1, new Outil("Superman","Avec des super pouvoirs aussi puissants que les miens, moi, SuperArthur, je serai inéffrayable!", Objet.Type.Skin, Outil.Portee.Nulle, 40, 0, 0, "arthur7_1", false));

        boutique.add(outilsBout);
        boutique.add(skinsBout);
        boutique.add(decorationsBout);
        inventaire.add(outilsInv);
        inventaire.add(skinsInv);
        inventaire.add(decorationsInv);
    }
    Joueur(String prenom, String nom, String addresseElectronique, int biscuits)
    {
        this.prenom = prenom;
        this.nom = nom;
        this.addresseElectronique = addresseElectronique;
        this.biscuits = biscuits;
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
    public String getAddresseElectronique()
    {
        return addresseElectronique;
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
        this.addresseElectronique = addresseElectronique;
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
}
