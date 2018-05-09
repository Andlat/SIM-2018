package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import static daynight.daynnight.MainActivity.joueur;

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
    Button utiliser;
    LinearLayout prixLayout;
    LinearLayout caracteristiquesOutil;

    TextView nom, prix, description, portee, nbCibles, toucherParCoup;
    ImageViewCarre imageObjet;

    //Constructeurs
    public PopupInformationsObjet() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_informations_objet);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        //Attribuer
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fermer = (Button) findViewById(R.id.fermerVerticale);
            boutons = (RelativeLayout) findViewById(R.id.BoutonsVerticale);
            acheter = (Button) findViewById(R.id.boutiqueAcheterVerticale);
            utiliser = (Button) findViewById(R.id.inventaireUtiliserVerticale);
            getWindow().setLayout((int) (width * 0.85), (int) (height * 0.75));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fermer = (Button) findViewById(R.id.fermerHorizontale);
            boutons = (RelativeLayout) findViewById(R.id.BoutonsHorizontale);
            acheter = (Button) findViewById(R.id.boutiqueAcheterHorizontale);
            utiliser = (Button) findViewById(R.id.inventaireUtiliserHorizontale);
            getWindow().setLayout((int) (width * 0.75), (int) (height * 0.85));
        }
        boutons.setVisibility(View.VISIBLE);

        prixLayout = (LinearLayout) findViewById(R.id.boutiquePrix);
        ViewGroup.LayoutParams paramsPrix = prixLayout.getLayoutParams();
        MarginLayoutParams paramsAcheter = (MarginLayoutParams) acheter.getLayoutParams();
        MarginLayoutParams paramsUtiliser = (MarginLayoutParams) utiliser.getLayoutParams();
        objetVendu = getIntent().getExtras().getBoolean("objetVendu");
        objet = getIntent().getExtras().getParcelable("objet");

        caracteristiquesOutil = (LinearLayout) findViewById(R.id.caracteristiquesOutil);
        nom = (TextView) findViewById(R.id.nom);
        prix = (TextView) findViewById(R.id.prix);
        description = (TextView) findViewById(R.id.description);
        portee = (TextView) findViewById(R.id.portee);
        nbCibles = (TextView) findViewById(R.id.nbCibles);
        toucherParCoup = (TextView) findViewById(R.id.toucherParCoup);
        imageObjet = (ImageViewCarre) findViewById(R.id.imageObjet);
        nom.setText(objet.getNom());
        prix.setText(String.valueOf(objet.getPrix()));
        description.setText(objet.getDescription());
        portee.setText(getString(R.string.portee, String.valueOf(objet.getPortee())));
        nbCibles.setText(getString(R.string.nb_cibles, objet.getNbCibles()));
        toucherParCoup.setText(getString(R.string.toucher_par_coup, objet.getToucherParCoup()));
        imageObjet.setImageResource(getResources().getIdentifier(objet.getImageDrawableString(), "drawable", getPackageName()));

        if (!objet.getAcquis())
        {
            //paramsPrix.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            //paramsAcheter.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsUtiliser.width = 0;
            paramsUtiliser.setMarginEnd(0);

            acheter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(MainActivity.joueur.getBiscuits() >= objet.getPrix())
                    {
                        objet.setAcquis(true);
                        if(objet.getType() == Objet.Type.Outil)
                        {
                            MainActivity.joueur.getOutilsInventaire().set(getIntent().getExtras().getInt("position"), objet);
                            MainActivity.joueur.getOutilsBoutique().set(getIntent().getExtras().getInt("position"), new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, "", true));
                            //ListeObjets.adapteur.retirementView(getIntent().getExtras().getInt("position"));
                            //ListeObjets.adapteur.notifyDataSetChanged();
                            //Boutique.viewPager.getAdapter().notifyDataSetChanged();
                            //Boutique.adapteurDeSection.notifyDataSetChanged();
                        }
                        else if(objet.getType() == Objet.Type.Skin)
                        {
                            MainActivity.joueur.getSkinsInventaire().set(getIntent().getExtras().getInt("position"), objet);
                            MainActivity.joueur.getSkinsBoutique().set(getIntent().getExtras().getInt("position"), new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, "", true));
                        }
                        else if(objet.getType() == Objet.Type.Décoration)
                        {
                            MainActivity.joueur.getDecorationsInventaire().set(getIntent().getExtras().getInt("position"), objet);
                            MainActivity.joueur.getDecorationsBoutique().set(getIntent().getExtras().getInt("position"), new Outil("Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, "", true));
                        }
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits()-objet.getPrix());
                        Boutique.biscuits.setText(String.valueOf(MainActivity.joueur.getBiscuits()));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(PopupInformationsObjet.this, "Maman- Arthur, arrête de manger des biscuits, tu vas devenir obèse!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if (objet.getAcquis())
        {
            paramsUtiliser.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            //paramsPrix.width = 0;
            //paramsAcheter.width = 0;
            //paramsAcheter.setMarginEnd(0);

            utiliser.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(objet.getType() == Objet.Type.Outil)
                    {
                        for(int i = 0 ; i < 4 ; i++)
                        {
                            startActivity(new Intent(PopupInformationsObjet.this, BarreDOutils.class));//TODO
                            BarreDOutils.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                                {
                                    BarreDOutils.outils[i] = objet;
                                    BarreDOutils.adapteur.getOutils()[i] = objet;
                                }
                            });
                        }
                    }
                    else if(objet.getType() == Objet.Type.Skin)
                    {

                    }
                    else if(objet.getType() == Objet.Type.Décoration)
                    {
                        //TODO
                    }
                    finish();
                }
            });
        }
        prixLayout.setLayoutParams(paramsPrix);
        acheter.setLayoutParams(paramsAcheter);
        utiliser.setLayoutParams(paramsUtiliser);

        if (objet.getType().equals(Objet.Type.Nul)) {}
        else if(objet.getType().equals(Objet.Type.Outil))
        {
            caracteristiquesOutil.setVisibility(View.VISIBLE);
        }
        else
        {
            caracteristiquesOutil.setVisibility(View.INVISIBLE);
        }

        fermer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    @Override
    protected void onStop()
    {
        MainActivity.ma.sauvegardeJoueur(joueur);
        MainActivity.musiqueDeFond.pause();
        super.onStop();
    }
    @Override
    protected void onResume()
    {
        MainActivity.musiqueDeFond.start();
        super.onResume();
    }

    //Getteurs & setteurs

    //Méthodes
    public void startActivity(Outil objet, int position, Context context, Boolean objetVendu)
    {
        Log.e("SEB", objet.getNom());
        Intent intent = new Intent(context, PopupInformationsObjet.class);
        //intent.putExtra("objetVendu", objetVendu);
        intent.putExtra("objet", objet);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
