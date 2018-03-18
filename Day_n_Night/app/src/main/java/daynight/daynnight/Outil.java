package daynight.daynnight;


import java.util.ArrayList;

/**
 * Created by sebastien on 18-03-12.
 */

public class Outil extends Objet
{
    //Variables
    enum Portee{Rapprochée, Éloignée} Portee portee;
    int nbCibles;
    int toucherParCoup;

    //Constructeurs
    Outil() {}
    Outil(String nom, String description, Portee portee, int prix, int toucherParCoup, int nbCibles, ArrayList<String> imagePaths)
    {
        super(nom, description, prix, imagePaths);

        this.portee = portee;
        this.toucherParCoup = toucherParCoup;
        this.nbCibles = nbCibles;
    }

    //Getteurs & Setteurs
    public Portee getPortee()
    {
        return portee;
    }
    public int getToucherParCoup()
    {
        return toucherParCoup;
    }
    public int getNbCibles()
    {
        return nbCibles;
    }

    public void setPortee(Portee portee)
    {
        this.portee = portee;
    }
    public void setToucherParCoup(int toucherParCoup)
    {
        this.toucherParCoup = toucherParCoup;
    }
    public void setNbCibles(int nbCibles)
    {
        this.nbCibles = nbCibles;
    }
}
