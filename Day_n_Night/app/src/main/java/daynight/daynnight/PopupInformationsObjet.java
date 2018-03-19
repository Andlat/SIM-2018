package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * Created by sebastien on 18-03-17.
 */

public class PopupInformationsObjet extends Activity
{
    //Variables
    Objet objet;
    Boolean objetVendu; //Boutique ou Inventaire
    RelativeLayout boutons;
    Button fermer;

    Button acheter;
    LinearLayout prix;

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

        prix = (LinearLayout) findViewById(R.id.boutiquePrix);
        ViewGroup.LayoutParams paramsPrix = prix.getLayoutParams();
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
        prix.setLayoutParams(paramsPrix);
        acheter.setLayoutParams(paramsAcheter);

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
    public void startActivity(Context context, Boolean objetVendu)
    {
        Intent intent = new Intent(context, PopupInformationsObjet.class);
        intent.putExtra("objetVendu", objetVendu);
        context.startActivity(intent);
    }

}
