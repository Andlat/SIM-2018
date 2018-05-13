package daynight.daynnight;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static daynight.daynnight.MainActivity.SurChangementActivity;


public class ListeObjets extends Fragment
{
    //Variables
    static GridView gridView;
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
        final ArrayList<Outil> objets = new ArrayList<>();
        adapteur = new AdapteurArrayaObjets(getContext(), 0, objets);
        gridView = getView().findViewById(R.id.listeObjets);
        gridView.setAdapter(adapteur);
        infosObjetInventaire = new PopupInformationsObjet();

        //Ajout des objets manuellement
        ArrayList<Outil> transitition = getArguments().getParcelableArrayList("objets");
        for(int j = 0 ; j < transitition.size() ; j++)
            objets.add(j, transitition.get(j));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                infosObjetInventaire.startActivity(objets.get(position), position, getContext(), objets.get(position).getAcquis());
                SurChangementActivity = true;
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
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), getResources().getIdentifier(nomObjet, "drawable", getContext().getPackageName()));
            bitmap = getBitmapGrandeur(bitmap, 70);
            objet.setImageBitmap(bitmap);

            return view;
        }
        public List<Outil> getObjets()
        {
            return this.objets;
        }
    }
    public Bitmap getBitmapGrandeur(Bitmap image, int maxSize)
    {
        int width, height;
        if(image != null)
        {
            width = image.getWidth();
            height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1)
            {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else
            {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }
        else
        {
           return image;
        }
    }
}
