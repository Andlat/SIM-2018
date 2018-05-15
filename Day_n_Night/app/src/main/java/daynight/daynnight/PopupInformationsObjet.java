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

import static daynight.daynnight.MainActivity.SurChangementActivity;
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

    Button utiliserAcheter;
    LinearLayout prixLayout;
    LinearLayout caracteristiquesOutil;

    TextView nom, prix, description, rarete, portee, nbCibles, toucherParCoup, intervalleParCoup;
    ImageViewCarre imageObjet;

    ChoixBarreDOutils choixBarreDOutils;

    //Constructeurs
    public PopupInformationsObjet() {}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_informations_objet);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        //Attribuer
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fermer = findViewById(R.id.fermerVerticale);
            boutons = findViewById(R.id.BoutonsVerticale);
            utiliserAcheter = findViewById(R.id.utiliserAcheterVerticale);
            getWindow().setLayout((int) (width * 0.85), (int) (height * 0.75));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fermer = findViewById(R.id.fermerHorizontale);
            boutons = findViewById(R.id.BoutonsHorizontale);
            utiliserAcheter = findViewById(R.id.utiliserAcheterHorizontale);
            getWindow().setLayout((int) (width * 0.75), (int) (height * 0.85));
        }
        boutons.setVisibility(View.VISIBLE);

        prixLayout = findViewById(R.id.boutiquePrix);
        ViewGroup.LayoutParams paramsPrix = prixLayout.getLayoutParams();
        objetVendu = getIntent().getExtras().getBoolean("objetVendu");
        objet = getIntent().getExtras().getParcelable("objet");
        choixBarreDOutils = new ChoixBarreDOutils();

        caracteristiquesOutil = findViewById(R.id.caracteristiquesOutil);
        nom = findViewById(R.id.nom);
        prix = findViewById(R.id.prix);
        description = findViewById(R.id.description);
        rarete  = findViewById(R.id.rarete);
        portee = findViewById(R.id.portee);
        nbCibles = findViewById(R.id.nbCibles);
        toucherParCoup = findViewById(R.id.toucherParCoup);
        intervalleParCoup = findViewById(R.id.intervalleParCoup);
        imageObjet = findViewById(R.id.imageObjet);
        nom.setText(objet.getNom());
        prix.setText(String.valueOf(objet.getPrix()));
        description.setText(objet.getDescription());
        switch (objet.getRarete())
        {
            case 1:
                rarete.setText(getString(R.string.rarete, "Régulier"));
                break;
            case 2:
                rarete.setText(getString(R.string.rarete, "Rare"));
                break;
            case 3:
                rarete.setText(getString(R.string.rarete, "Légendaire"));
                break;
            default:
                rarete.setText(getString(R.string.rarete, "Aucune"));
                break;
        }
        portee.setText(getString(R.string.portee, String.valueOf(objet.getPortee())));
        nbCibles.setText(getString(R.string.nb_cibles, objet.getNbCibles()));
        toucherParCoup.setText(getString(R.string.toucher_par_coup, objet.getToucherParCoup()));
        intervalleParCoup.setText(getString(R.string.intervalle_par_coup, objet.getIntervalleParCoup()));
        imageObjet.setImageResource(getResources().getIdentifier(objet.getImageDrawableString(), "drawable", getPackageName()));

        if (!objet.getAcquis())
        {
            paramsPrix.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            utiliserAcheter.setText(getString(R.string.acheter));

            utiliserAcheter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(MainActivity.joueur.getBiscuits() >= objet.getPrix())
                    {
                        objet.setAcquis(true);
                        if (objet.getType() == Objet.Type.Outil)
                        {
                            MainActivity.joueur.getOutilsInventaire().add(objet);
                            MainActivity.joueur.getOutilsBoutique().set(getIntent().getExtras().getInt("position"), new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));
                        } else if (objet.getType() == Objet.Type.Skin)
                        {
                            MainActivity.joueur.getSkinsInventaire().add(objet);
                            MainActivity.joueur.getSkinsBoutique().set(getIntent().getExtras().getInt("position"), new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));
                        } else if (objet.getType() == Objet.Type.Décoration)
                        {
                            MainActivity.joueur.getDecorationsInventaire().add(objet);
                            MainActivity.joueur.getDecorationsBoutique().set(getIntent().getExtras().getInt("position"), new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));
                        }
                        finish();
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() - objet.getPrix());
                        Boutique.biscuits.setText(String.valueOf(MainActivity.joueur.getBiscuits()));
                        Boutique.boutique.finish();
                        startActivity(Inventaire.inventaire.getIntent());
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
            paramsPrix.width = 0;
            utiliserAcheter.setText(getString(R.string.utiliser));

            utiliserAcheter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(objet.getType() == Objet.Type.Outil)
                    {
                        //Bundle bundle = new Bundle();
                        //bundle.putParcelable("outil", objet);
                        //Log.e("TEST", objet.getDescription());
                        //BarreDOutils.barreDOutils = new BarreDOutils();
                        //BarreDOutils.barreDOutils.getArguments().putParcelable("outil", objet);
                        //BarreDOutils barreDOutils = new BarreDOutils();
                        //barreDOutils.getArguments().putParcelable("outil", objet);
                        //choixBarreDOutils.getFragmentManager().beginTransaction().replace(R.id.barreDOutils, barreDOutils).commit();
                        choixBarreDOutils.startActivity(getApplicationContext());
                    }
                    else if(objet.getType() == Objet.Type.Skin)
                    {
                        MainActivity.joueur.setSkin(getResources().getIdentifier(objet.getImageDrawableString(), "drawable", getPackageName()));
                    }
                    else if(objet.getType() == Objet.Type.Décoration)
                    {
                        //TODO
                    }
                    finish();
                    SurChangementActivity = false;
                }
            });
        }
        prixLayout.setLayoutParams(paramsPrix);

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
                SurChangementActivity = false;
            }
        });
    }
    @Override
    protected void onStop()
    {
        if(SurChangementActivity)
        {
            MainActivity.musiqueDeFond.pause();
            MainActivity.ma.sauvegardeJoueur(joueur);
        }
        super.onStop();
    }
    @Override
    protected void onResume()
    {
        if(MainActivity.joueur.getMusique())
        {
            MainActivity.musiqueDeFond.start();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SurChangementActivity = false;
    }

    //Méthodes
    public void startActivity(Outil objet, int position, Context context, Boolean objetVendu)
    {
        Intent intent = new Intent(context, PopupInformationsObjet.class);
        //intent.putExtra("objetVendu", objetVendu);
        intent.putExtra("objet", objet);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
