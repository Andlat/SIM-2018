package daynight.daynnight;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sebastien on 18-03-12.
 */

public class Outil extends Objet implements Parcelable
{
    //Variables
    enum Portee{Rapprochée, Éloignée} Portee portee;
    int nbCibles;
    int toucherParCoup;

    //Constructeurs
    Outil() {}
    Outil(String nom, String description, Portee portee, int prix, int toucherParCoup, int nbCibles/*, ArrayList<String> imagePaths*/, String imageDrawableString)
    {
        super(nom, description, prix/*, imagePaths*/, imageDrawableString);

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

    //Parceable
    public Outil(Parcel in)
    {
        super(in.readString(), in.readString(), in.readInt()/*, (ArrayList<String>) in.readSerializable()*/, in.readString());

        this.portee = Portee.valueOf(in.readString());
        this.toucherParCoup = in.readInt();
        this.nbCibles = in.readInt();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i)
    {
        out.writeString(this.nom);
        out.writeString(this.description);
        out.writeString(this.portee.name());
        out.writeInt(this.prix);
        out.writeInt(this.toucherParCoup);
        out.writeInt(this.nbCibles);
        //out.writeSerializable(this.imagePaths);
        out.writeString(this.imageDrawableString);
    }
    public static final Parcelable.Creator<Outil> CREATOR = new Parcelable.Creator<Outil>()
    {
        public Outil createFromParcel(Parcel in)
        {
            return new Outil(in);
        }

        public Outil[] newArray(int size)
        {
            return new Outil[size];
        }
    };
}
