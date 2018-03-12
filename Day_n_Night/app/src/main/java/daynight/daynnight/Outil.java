package daynight.daynnight;


import java.util.ArrayList;

/**
 * Created by sebastien on 18-03-12.
 */

public class Outil
{
    //Variables
    String nom;
    String description;
    enum Type{Rapproché, Distant} Type type;
    enum Portee{Multiple, Simple} Portee portee;
    int prix;
    int toucherParCoup;

    ArrayList<String> imagePaths;

    //Constructeurs
    Outil() {}
    Outil(String nom,String description, Type type, Portee portee, int prix, int toucherParCoup, ArrayList<String> imagePaths)
    {
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.portee = portee;
        this.prix = prix;
        this.toucherParCoup = toucherParCoup;
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
    public Type getType()
    {
        return type;
    }
    public Portee getPortee()
    {
        return portee;
    }
    public int getPrix()
    {
        return prix;
    }
    public int getToucherParCoup()
    {
        return toucherParCoup;
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
    public void setType(Type type)
    {
        this.type = type;
    }
    public void setPortee(Portee portee)
    {
        this.portee = portee;
    }
    public void setPrix(int prix)
    {
        this.prix = prix;
    }
    public void setToucherParCoup(int toucherParCoup)
    {
        this.toucherParCoup = toucherParCoup;
    }
    public void setImagePaths(ArrayList<String> imagePaths)
    {
        this.imagePaths = imagePaths;
    }
}
