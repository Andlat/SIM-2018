package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class Inventaire extends AppCompatActivity
{
    //Créer
    Button retour;
    ImageView boutique;
    static ArrayList<String> nomObjets = new ArrayList<>();
    static Inventaire.AdapteurArrayInventaire adapteur;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inventaire);


        adapteur = new Inventaire.AdapteurArrayInventaire(this, 0, nomObjets);

        final GridView gridView = (GridView) findViewById(R.id.inventaire);
        gridView.setAdapter(adapteur);


        //Attribuer
        retour = (Button) findViewById(R.id.retour);
        boutique = (ImageView) findViewById(R.id.boutique);

        //Ajout de Badges manuellement
        for(int j = 0 ; j < 48 ; j++)
            nomObjets.add("");
        nomObjets.set(0,"outil_eau_benite");
        nomObjets.set(1,"outil_melon_deau");
        nomObjets.set(2,"outil_seau_deau");
        nomObjets.set(3,"outil_boule_neige");



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;
        boutique.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xCoord = event.getRawX() - view.getWidth()/2;
                        float yCoord = event.getRawY() - view.getHeight()/(1.2f);

                        if((xCoord + view.getWidth()) < width && (xCoord) > 0)
                        {
                            layoutParams.leftMargin = (int) xCoord;
                        }
                        if((yCoord + view.getHeight()*(1.3f)) < height && (yCoord) > 0)
                        {
                            layoutParams.topMargin = (int) yCoord;
                        }

                        view.setLayoutParams(layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

        /*boutique.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Inventaire.this, Boutique.class));
            }
        });*/
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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            gridView.setNumColumns(7);
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            gridView.setNumColumns(4);
        }
    }

    //custom ArrayAdapter
    class AdapteurArrayInventaire extends ArrayAdapter<String>
    {
        private Context context;
        private int layout;
        private List<String> nomObjets;

        public AdapteurArrayInventaire(Context context, int resource, ArrayList<String> objects)
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

            GridViewInventaireObjet objet = view.findViewById(R.id.objet);
            objet.setImageResource(getResources().getIdentifier(nomObjet, "drawable", getPackageName()));


            return view;
        }
    }
}
