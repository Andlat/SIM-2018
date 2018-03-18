package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by sebastien on 18-03-17.
 */

public class PopupInformationsObjet extends Activity
{
    //Variables
    Objet objet;
    Boolean objetVendu; //Boutique ou Inventaire
    Button fermer;

    //Constructeurs
    PopupInformationsObjet(){}
    PopupInformationsObjet(/*Objet objet, */Boolean objetVendu)
    {
        //this.objet = objet;
        this.objetVendu = objetVendu;
    }

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
            getWindow().setLayout((int)(width*0.85), (int)(height*0.75));
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            fermer = (Button) findViewById(R.id.fermerHorizontale);
            getWindow().setLayout((int)(width*0.80), (int)(height*0.80));
        }
        fermer.setVisibility(View.VISIBLE);

        fermer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
}
