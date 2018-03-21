package daynight.daynnight;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sebastien on 18-03-15.
 */

public class Objet implements Parcelable
{
    //Variables
    String nom;
    String description;
    enum Type{Outil, Skin, Décoration} Type type;

    int prix;

    //ArrayList<String> imagePaths;
    String imageDrawableString;

    //Constructeurs
    Objet() {}
    Objet(String nom, String description, int prix/*, ArrayList<String> imagePaths*/, String imageDrawableString)
    {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        //this.imagePaths = imagePaths;
        this.imageDrawableString = imageDrawableString;
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
    /*public ArrayList<String> getImagePaths()
    {
        return imagePaths;
    }*/
    public Type getType()
    {
        return type;
    }
    public String getImageDrawableString()
    {
        return imageDrawableString;
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
    /*public void setImagePaths(ArrayList<String> imagePaths)
    {
        this.imagePaths = imagePaths;
    }*/
    public void setType(Type type)
    {
        this.type = type;
    }
    public void setImageDrawableString(String imageDrawableString)
    {
        this.imageDrawableString = imageDrawableString;
    }

    //Parceable
    public Objet(Parcel in)
    {
        this.nom = in.readString();
        this.description = in.readString();
        this.prix = in.readInt();
        //this.imagePaths = (ArrayList<String>) in.readSerializable();
        this.imageDrawableString = in.readString();
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
        out.writeInt(this.prix);
        //out.writeSerializable(this.imagePaths);
        out.writeString(this.imageDrawableString);
    }
    public static final Parcelable.Creator<Objet> CREATOR = new Parcelable.Creator<Objet>()
    {
        public Objet createFromParcel(Parcel in)
        {
            return new Objet(in);
        }

        public Objet[] newArray(int size)
        {
            return new Objet[size];
        }
    };
}
