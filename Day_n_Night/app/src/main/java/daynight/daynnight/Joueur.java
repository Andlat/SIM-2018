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

    List<ArrayList<Outil>> inventaire;
    List<ArrayList<Outil>> boutique;
    List<Badge> badges;

    //Préférences
    Boolean musique;


    //Constructeurs
    Joueur()
    {
        this.prenom = "Arthur";
        this.nom = "Ça rie, Sarry pu";//wow
        this.addresseElectronique = "baguettefrancaise@hotmail.com";
        inventaire = new ArrayList<>(3);
        inventaire.add(new ArrayList<Outil>());
        inventaire.add(new ArrayList<Outil>());
        inventaire.add(new ArrayList<Outil>());

        boutique = new ArrayList<>(3);
        ArrayList<Outil> outilsBout = new ArrayList<>();
        outilsBout.add(new Outil("Seau d'eau","Le seau d'eau ne contient pas de l'eau, mais plutôt de la Vodka", Objet.Type.Outil, Outil.Portee.Éloignée,6,1,1,"objet_outil_seau_deau", false));
        outilsBout.add(new Outil("Master-Ball","La Master-Ball est une Poké-Ball utilisée par les meilleurs dresseurs de pokémons dans Pokémons, il faut être un maitre dans l'art pour l'utiliser!", Objet.Type.Outil, Outil.Portee.Éloignée,20,3,1,"objet_outil_masterball", false));
        boutique.add(outilsBout);
        ArrayList<Outil> skinsBout = new ArrayList<>();
        skinsBout.add(new Outil("Pijama","Un pijama rend nos nuits beaucoup plus conforatbles, n'est-ce pas ?", Objet.Type.Skin, Outil.Portee.Nulle, 20, 0, 0, "arthur2_1", false));
        skinsBout.add(new Outil("Superman","Avec des super pouvoirs aussi puissants que les miens, moi, SuperArthur, je serai inéffrayable!", Objet.Type.Skin, Outil.Portee.Nulle, 40, 0, 0, "arthur7_1", false));
        boutique.add(skinsBout);
        boutique.add(new ArrayList<Outil>());
    }
    Joueur(String prenom, String nom, String addresseElectronique, List<ArrayList<Outil>> inventaire)
    {
        this.prenom = prenom;
        this.nom = nom;
        this.addresseElectronique = addresseElectronique;
        this.inventaire = inventaire;
    }

    //Getteurs & Setteurs
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
