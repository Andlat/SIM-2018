package daynight.daynnight;


import java.util.ArrayList;

/**
 * Created by sebastien on 18-03-12.
 */

public class Outil extends Objet
{
    //Variables
    enum Portee{Rapprochée, Éloignée} Portee portee;
    enum Cible{Multiple, Simple} Cible cible;
    int toucherParCoup;

    //Constructeurs
    Outil() {}
    Outil(String nom, String description, Portee portee, Cible cible, int prix, int toucherParCoup, ArrayList<String> imagePaths)
    {
        super(nom, description, prix, imagePaths);

        this.portee = portee;
        this.cible = cible;
        this.toucherParCoup = toucherParCoup;
    }

    //Getteurs & Setteurs
    public Portee getPortee()
    {
        return portee;
    }
    public Cible getCible()
    {
        return cible;
    }
    public int getToucherParCoup()
    {
        return toucherParCoup;
    }

    public void setPortee(Portee portee)
    {
        this.portee = portee;
    }
    public void setCible(Cible cible)
    {
        this.cible = cible;
    }
    public void setToucherParCoup(int toucherParCoup)
    {
        this.toucherParCoup = toucherParCoup;
    }
}
