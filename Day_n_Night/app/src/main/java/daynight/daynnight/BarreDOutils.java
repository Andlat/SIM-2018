package daynight.daynnight;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class BarreDOutils extends Fragment
{
    //Variables
    static GridView gridView;
    static AdapteurArrayaObjets adapteur;
    PopupInformationsObjet infosObjetInventaire;
    static Outil[] outils = new Outil[5];

    //constructeur
    public BarreDOutils() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_barre_doutils, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        for(int i = 0 ; i < 5 ; i++)
            outils[i] = new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration,0, Outil.Portee.Nulle, 0, 0, 0,0f, "", true);

        adapteur = new AdapteurArrayaObjets(getContext(), 0, outils);
        gridView = getView().findViewById(R.id.listeOutils);
        gridView.setAdapter(adapteur);
        infosObjetInventaire = new PopupInformationsObjet();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                infosObjetInventaire.startActivity(outils[position], position, getContext(), outils[position].getAcquis());
            }
        });

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
    }
}
