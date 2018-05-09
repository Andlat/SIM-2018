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
    int id;
    String nom;
    String description;
    enum Type{Outil, Skin, Décoration, Nul} Type type;
    int rarete;
    //TODO int identifiant;

    int prix;

    //ArrayList<String> imagePaths;
    String imageDrawableString;

    Boolean acquis;

    //Constructeurs
    Objet() {}
    Objet(int id, String nom, String description, Type type, int rarete, int prix, String imageDrawableString, Boolean acquis)
    {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.rarete = rarete;
        this.prix = prix;
        this.imageDrawableString = imageDrawableString;
        this.acquis = acquis;
    }

    //Getteurs & Setteurs
    public int getId()
    {
        return id;
    }
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
    public Type getType()
    {
        return type;
    }
    public int getRarete()
    {
        return rarete;
    }
    public String getImageDrawableString()
    {
        return imageDrawableString;
    }
    public Boolean getAcquis()
    {
        return acquis;
    }

    public void setId(int id)
    {
        this.id = id;
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
    public void setType(Type type)
    {
        this.type = type;
    }
    public void setRarete(int rarete)
    {
        this.rarete = rarete;
    }
    public void setImageDrawableString(String imageDrawableString)
    {
        this.imageDrawableString = imageDrawableString;
    }
    public void setAcquis(Boolean acquis)
    {
        this.acquis = acquis;
    }

    //Parceable
    public Objet(Parcel in)
    {
        this.id = in.readInt();
        this.nom = in.readString();
        this.description = in.readString();
        this.type = Type.valueOf(in.readString());
        this.rarete = in.readInt();
        this.prix = in.readInt();
        this.imageDrawableString = in.readString();
        this.acquis = Boolean.valueOf(in.readString());
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i)
    {
        out.writeInt(this.id);
        out.writeString(this.nom);
        out.writeString(this.description);
        out.writeString(this.type.name());
        out.writeInt(this.rarete);
        out.writeInt(this.prix);
        out.writeString(this.imageDrawableString);
        out.writeString(this.acquis.toString());
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
