package daynight.daynnight;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.support.design.widget.CoordinatorLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

public class Inventaire extends AppCompatActivity
{
    //Créer
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
        boutique = (ImageView) findViewById(R.id.boutique);
        //Ajout de Badges manuellement
        for(int j = 0 ; j < 48 ; j++)
            nomObjets.add("");
        nomObjets.set(0,"objet_outil_eau_benite");
        nomObjets.set(1,"objet_outil_melon_deau");
        nomObjets.set(2,"objet_outil_seau_deau");
        nomObjets.set(3,"objet_outil_boule_neige");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;

        boutique.setOnTouchListener(new View.OnTouchListener()
        {
            float xRepere;
            float yRepere;
            float xCoord, yCoord;
            float Xdiff = 11;
            float Ydiff = 11;

            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                layoutParams.gravity = Gravity.NO_GRAVITY;

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        xRepere = event.getRawX();
                        yRepere = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        Xdiff = event.getRawX() - xRepere;
                        Ydiff = event.getRawY() - yRepere;


                        if (Xdiff <= 10 && Ydiff <= 10 && Xdiff >= -10 && Ydiff >= -10)
                        {
                            view.performClick();
                            startActivity(new Intent(Inventaire.this, Boutique.class));
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xCoord = event.getRawX() - view.getWidth()/2;
                        yCoord = event.getRawY() - view.getHeight()/(1.2f);

                        if (Xdiff > 10 || Ydiff > 10 || Xdiff < -10 || Ydiff < -10)
                        {
                            if((xCoord + view.getWidth()) < width && (xCoord) > 0)
                            {
                                layoutParams.leftMargin = (int) xCoord;
                            }
                            if((yCoord + view.getHeight()*(1.3f)) < (height - (width/5)) && (yCoord) > 0)
                            {
                                layoutParams.topMargin = (int) yCoord;
                            }
                        }
                        view.setLayoutParams(layoutParams);
                        break;
                }
                return true;
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

            ObjetGridView objet = view.findViewById(R.id.objet);
            objet.setImageResource(getResources().getIdentifier(nomObjet, "drawable", getPackageName()));


            return view;
        }
    }
}
