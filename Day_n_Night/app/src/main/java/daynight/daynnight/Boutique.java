package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class Boutique extends AppCompatActivity
{
    //Créer
    Button retour;
    static ArrayList<String> nomObjets = new ArrayList<>();
    static AdapteurArrayBoutique adapteur;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_boutique);


        adapteur = new AdapteurArrayBoutique(this, 0, nomObjets);

        GridView gridView = (GridView) findViewById(R.id.boutique);
        gridView.setAdapter(adapteur);


        //Attribuer
        retour = (Button) findViewById(R.id.retour);

        //Ajout de Badges manuellement
        for(int j = 0 ; j < 48 ; j++)
            nomObjets.add("");
        nomObjets.set(0,"arthur1_1");
        nomObjets.set(1,"arthur2_1");


        retour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO
            }
        });
    }

    //custom ArrayAdapter
    class AdapteurArrayBoutique extends ArrayAdapter<String>
    {
        private Context context;
        private int layout;
        private List<String> nomObjets;

        public AdapteurArrayBoutique(Context context, int resource, ArrayList<String> objects)
        {
            super(context, resource, objects);

            this.context = context;
            this.layout = resource;
            this.nomObjets = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            String nomObjet = nomObjets.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_objet, null);
            view.setPaddingRelative(20,20,20,20);

            ImageViewCarre objet = view.findViewById(R.id.objet);
            objet.setImageResource(getResources().getIdentifier(nomObjet, "drawable", getPackageName()));


            return view;
        }
    }
}