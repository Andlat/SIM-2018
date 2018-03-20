package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

/**
 * Created by sebastien on 18-03-17.
 */

public class PopupInformationsObjet extends Activity
{
    //Variables
    Outil objet;
    Boolean objetVendu; //Boutique ou Inventaire
    RelativeLayout boutons;
    Button fermer;

    Button acheter;
    LinearLayout prixLayout;
    LinearLayout caracteristiquesOutil;

    TextView nom, prix, description, portee, nbCibles, toucherParCoup;
    ImageView imageObjet;

    Context context;

    //Constructeurs
    public PopupInformationsObjet() {}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_informations_objet);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            fermer = (Button) findViewById(R.id.fermerVerticale);
            boutons = (RelativeLayout) findViewById(R.id.boutiqueBoutonsVerticale);
            acheter = (Button) findViewById(R.id.boutiqueAcheterVerticale);
            getWindow().setLayout((int)(width*0.85), (int)(height*0.75));
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            fermer = (Button) findViewById(R.id.fermerHorizontale);
            boutons = (RelativeLayout) findViewById(R.id.boutiqueBoutonsHorizontale);
            acheter = (Button) findViewById(R.id.boutiqueAcheterHorizontale);
            getWindow().setLayout((int)(width*0.80), (int)(height*0.80));
        }
        boutons.setVisibility(View.VISIBLE);

        prixLayout = (LinearLayout) findViewById(R.id.boutiquePrix);
        ViewGroup.LayoutParams paramsPrix = prixLayout.getLayoutParams();
        MarginLayoutParams paramsAcheter = (MarginLayoutParams) acheter.getLayoutParams();
        objetVendu = getIntent().getExtras().getBoolean("objetVendu");

        if(!objetVendu)
        {
            paramsPrix.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsAcheter.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            paramsPrix.width = 0;
            paramsAcheter.width = 0;
            paramsAcheter.setMarginEnd(0);
        }
        prixLayout.setLayoutParams(paramsPrix);
        acheter.setLayoutParams(paramsAcheter);


        objet = new Outil("Seau d'eau","Le seau d'eau ne contient pas de l'eau, mais plutôt de la Vodka", Outil.Portee.Éloignée,6,1,1,"objet_outil_seau_deau");
        //objet = getIntent().getParcelableExtra("objet");
        caracteristiquesOutil = (LinearLayout) findViewById(R.id.caracteristiquesOutil);
        nom = (TextView) findViewById(R.id.nom);
        prix = (TextView) findViewById(R.id.prix);
        description = (TextView) findViewById(R.id.description);
        portee = (TextView) findViewById(R.id.portee);
        nbCibles = (TextView) findViewById(R.id.nbCibles);
        toucherParCoup = (TextView) findViewById(R.id.toucherParCoup);
        imageObjet = (ImageView) findViewById(R.id.imageObjet);
        nom.setText(objet.getNom());
        prix.setText(String.valueOf(objet.getPrix()));
        description.setText(objet.getDescription());
        portee.setText(String.valueOf(objet.getPortee()));
        nbCibles.setText(String.valueOf(objet.getNbCibles()));
        toucherParCoup.setText(String.valueOf(objet.getToucherParCoup()));
        imageObjet.setImageResource(getResources().getIdentifier(objet.getImageDrawableString(), "drawable", getPackageName()));
        /*if(objet.getType().equals(Objet.Type.Outil))
        {
            caracteristiquesOutil.setVisibility(View.VISIBLE);

        }
        else
        {
            caracteristiquesOutil.setVisibility(View.INVISIBLE);
        }*/

        fermer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
    //Getteurs & setteurs

    //Méthodes
    public void startActivity(Objet objet, Context context, Boolean objetVendu)
    {
        Intent intent = new Intent(context, PopupInformationsObjet.class);
        intent.putExtra("objetVendu", objetVendu);
        //intent.putExtra("objet", objet); //TODO j'obtiens un erreur sur la valeur de l'enum Portee...
        context.startActivity(intent);
    }

}
