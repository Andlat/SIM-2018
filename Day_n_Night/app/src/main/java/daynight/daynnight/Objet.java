package daynight.daynnight;

import java.util.ArrayList;

/**
 * Created by sebastien on 18-03-15.
 */

public class Objet
{
    //Variables
    String nom;
    String description;

    int prix;

    ArrayList<String> imagePaths;

    //Constructeurs
    Objet() {}
    Objet(String nom, String description, int prix, ArrayList<String> imagePaths)
    {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.imagePaths = imagePaths;
    }

    //Getteurs & Setteurs
    public String getNom()
    {
        return nom;
    }
    public String getDescription()
    {
        return description;
    }
    public int getPrix()
    {
        return prix;
    }
    public ArrayList<String> getImagePaths()
    {
        return imagePaths;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public void setPrix(int prix)
    {
        this.prix = prix;
    }
    public void setImagePaths(ArrayList<String> imagePaths)
    {
        this.imagePaths = imagePaths;
    }
}
