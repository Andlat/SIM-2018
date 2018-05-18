package daynight.daynnight;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static daynight.daynnight.GameActivity.enJeu;
import static daynight.daynnight.Inventaire.choix;


public class BarreDOutils extends Fragment
{
    //Variables
    static BarreDOutils barreDOutils;
    static GridView gridView;
    static AdapteurArrayaObjets adapteur;
    PopupInformationsObjet infosObjetInventaire;
    static Outil[] outils = new Outil[5];
    Outil outil;

    //constructeur
    public BarreDOutils() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        barreDOutils = this;
        return inflater.inflate(R.layout.fragment_barre_doutils, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //Ajout des objets manuellement
        //Outil[] transition = getArguments().getParcelableArray("barreDOutils");
        for(int i = 0 ; i < 5 ; i++)
        {
            outils[i] = MainActivity.joueur.getBarreDOutils()[i];
            Log.e("TEST", outils[i].getNom());
        }

        adapteur = new AdapteurArrayaObjets(getContext(), 0, outils);
        gridView = getView().findViewById(R.id.listeOutils);
        gridView.setAdapter(adapteur);
        infosObjetInventaire = new PopupInformationsObjet();


        if(enJeu)
        {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    MainActivity.joueur.setOutilSelection(outils[i]);
                    //TODO ICI NIK
                }
            });
        }
        else if(choix)
        {
            outil = getArguments().getParcelable("outil");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    outils[i] = outil;
                    MainActivity.joueur.setBarreDOutils(outils);
                    adapteur.setOutil(outil, i);
                    adapteur.notifyDataSetChanged();
                    outil = null;
                    choix = false;
                    ChoixBarreDOutils.choixBarreDOutils.finish();
                    Inventaire.inventaire.finish();
                    startActivity(Inventaire.inventaire.getIntent());
                }
            });
        }
        else
        {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    infosObjetInventaire.startActivity(outils[position], position, view.getContext(), outils[position].getAcquis());
                }
            });
        }

        gridView.setNumColumns(5);
    }

    //custom ArrayAdapter
    class AdapteurArrayaObjets extends ArrayAdapter<Outil>
    {
        private Context context;
        private int layout;
        private Outil[] outils;

        public AdapteurArrayaObjets(Context context, int resource, Outil[] objects)
        {
            super(context, resource, objects);

            this.context = context;
            this.layout = resource;
            this.outils = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            String nomObjet = outils[position].getImageDrawableString();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_objet, null);
            view.setPaddingRelative(20,20,20,20);

            ImageViewCarre objet = view.findViewById(R.id.objet);
            objet.setImageResource(getResources().getIdentifier(nomObjet, "drawable", getContext().getPackageName()));

            return view;
        }
        public Outil[] getOutils()
        {
            return this.outils;
        }
        public void setOutil(Outil outil, int position)
        {
            outils[position] = outil;
        }
    }
}
