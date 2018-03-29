package daynight.daynnight;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class ListeObjets extends Fragment
{
    //Variables
    GridView gridView;
    ArrayList<Outil> objets = new ArrayList<>();
    static AdapteurArrayaObjets adapteur;
    PopupInformationsObjet infosObjetInventaire;

    //Constructeur
    public ListeObjets() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_liste_objets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        adapteur = new AdapteurArrayaObjets(getContext(), 0, objets);
        gridView = (GridView) getView().findViewById(R.id.listeObjets);
        gridView.setAdapter(adapteur);
        infosObjetInventaire = new PopupInformationsObjet();


        //Ajout d'e cases vides
        for(int j = 0 ; j < 28 ; j++)
            objets.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.",Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, "", true));

        //Ajout des objets manuellement
        ArrayList<Outil> transitition = getArguments().getParcelableArrayList("objets");
        for(int j = 0 ; j < transitition.size() ; j++)
            objets.set(j, transitition.get(j));




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                infosObjetInventaire.startActivity(objets.get(position), getContext(), objets.get(position).getAcquis());
            }
        });

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            gridView.setNumColumns(4);
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            gridView.setNumColumns(7);
        }
    }

    //Méthodes
    public static ListeObjets newInstance(ArrayList<Outil> objets)
    {
        ListeObjets fragment = new ListeObjets();
        Bundle bundle = new Bundle();
        bundle.putSerializable("objets", objets);
        fragment.setArguments(bundle);
        return fragment;
    }

    //custom ArrayAdapter
    class AdapteurArrayaObjets extends ArrayAdapter<Outil>
    {
        private Context context;
        private int layout;
        private List<Outil> objets;

        public AdapteurArrayaObjets(Context context, int resource, ArrayList<Outil> objects)
        {
            super(context, resource, objects);

            this.context = context;
            this.layout = resource;
            this.objets = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            String nomObjet = objets.get(position).getImageDrawableString();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_objet, null);
            view.setPaddingRelative(20,20,20,20);

            ImageViewCarre objet = view.findViewById(R.id.objet);
            objet.setImageResource(getResources().getIdentifier(nomObjet, "drawable", getContext().getPackageName()));


            return view;
        }
    }
}
