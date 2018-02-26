package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListeBadges extends AppCompatActivity
{
    //Créer
    Button nouvCompte, suppUnCompte;
    static ArrayList<CompteBancaire> comptesBancaires = new ArrayList<>();
    static AdapteurArrayCompteBancaire adapteur;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_liste_badges);

        adapteur = new AdapteurArrayCompteBancaire(this, 0, comptesBancaires);

        ListView listView = (ListView) findViewById(R.id.listeComptesBancaires);
        listView.setAdapter(adapteur);


        //Attribuer
        nouvCompte = (Button) findViewById(R.id.nouvCompte);
        suppUnCompte = (Button) findViewById(R.id.suppUnCompte);

        //Ajout des comptes bancaires manuellement
        //comptesBancaires.add(new CompteBancaire("Ciniseb","Galor","Sylphigle","Cartomancien",122,85));
        //comptesBancaires.add(new CompteBancaire("Léamsi","Galor","Zygöre","Druide",63,100));
        //comptesBancaires.add(new CompteBancaire("Gabrya","Winchester","Zygöre","Archer Élémentaire",56,90));

        nouvCompte.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(BanqueZirconienne.this, ListeJoueurs.class));
            }
        });
        suppUnCompte.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(suppUnCompte.getText() == "Annuler")
                {
                    suppUnCompte.setText("Supprimer\nun compte");
                    suppUnCompte.setBackgroundResource(R.drawable.icon_bouton);
                    adapteur.setAfficheBoutonSupp(false);
                }
                else
                {
                    suppUnCompte.setText("Annuler");
                    suppUnCompte.setBackgroundResource(R.drawable.icon_bouton_rouge);
                    adapteur.setAfficheBoutonSupp(true);
                }
                adapteur.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(BanqueZirconienne.this, CompteBancaire.class));
            }
        });

    }

    //custom ArrayAdapter
    class AdapteurArrayCompteBancaire extends ArrayAdapter<CompteBancaire>
    {
        private Context context;
        private int layout;
        private List<CompteBancaire> comptesBancaires;
        Boolean afficheBoutonSupp = null;

        public AdapteurArrayCompteBancaire(Context context, int resource, ArrayList<CompteBancaire> objects)
        {
            super(context, resource, objects);

            this.context = context;
            this.layout = resource;
            this.comptesBancaires = objects;
            afficheBoutonSupp = false;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            CompteBancaire compteBancaire = comptesBancaires.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_bouton_compte_bancaire, null);

            TextView prenomClient = (TextView) view.findViewById(R.id.prenomClient);
            TextView nomClient = (TextView) view.findViewById(R.id.nomClient);
            TextView ageClient = (TextView) view.findViewById(R.id.ageClient);
            TextView raceClient = (TextView) view.findViewById(R.id.raceClient);
            TextView classeClient = (TextView) view.findViewById(R.id.classeClient);
            TextView fiabiliteClient = (TextView) view.findViewById(R.id.fiabiliteClient);

            prenomClient.setText(compteBancaire.getPrenomClient());
            nomClient.setText(compteBancaire.getNomClient());
            ageClient.setText(getString(R.string.ageClient, compteBancaire.getAgeClient()));
            raceClient.setText(getString(R.string.raceClient, compteBancaire.getRaceClient()));
            classeClient.setText(getString(R.string.classeClient, compteBancaire.getClasseClient()));
            fiabiliteClient.setText(getString(R.string.fiabiliteClient, compteBancaire.getFiabilité()));


            return view;
        }
    }
}
