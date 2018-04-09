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

    List<List<Outil>> inventaire;
    List<List<Outil>> boutique;
    List<Badge> badges;

    //Préférences
    Boolean musique;


    //Constructeurs
    Joueur()
    {
        this.prenom = "Athur";
        this.nom = "Ça rie, Sarry pu";
        this.addresseElectronique = "baguettefrancaise@hotmail.com";
        inventaire = new ArrayList<>(3);
        inventaire.add(new ArrayList<Outil>());//Outils
        inventaire.add(new ArrayList<Outil>());//Skins
        inventaire.add(new ArrayList<Outil>());//Décorations
    }
    Joueur(String prenom, String nom, String addresseElectronique, List<List<Outil>> inventaire)
    {
        this.prenom = prenom;
        this.nom = nom;
        this.addresseElectronique = addresseElectronique;
        this.inventaire = inventaire;
    }

    //Getteurs & Setteurs
    public List<Outil> getOutils()
    {
        return inventaire.get(0);
    }
    public List<Outil> getSkins()
    {
        return inventaire.get(1);
    }
    public List<Outil> getDecorations()
    {
        return inventaire.get(2);
    }

    public void setOutils(List<Outil> outils)
    {
        this.inventaire.set(0, outils);
    }
    public void setSkins(List<Outil> skins)
    {
        this.inventaire.set(1, skins);
    }
    public void setDecorations(List<Outil> decorations)
    {
        this.inventaire.set(2, decorations);
    }

    //Méthodes
}
