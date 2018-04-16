package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import static daynight.daynnight.ListeObjets.newInstance;

public class Inventaire extends AppCompatActivity
{
    //Variables
    ImageView boutique;

    TabLayout tabObjets;
    ViewPager viewPager;

    static ArrayList<Outil> outils;
    static ArrayList<Outil> skins;
    static ArrayList<Outil> decorations;

    //Constructeurs
    Inventaire()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inventaire);

        //Attribuer
        boutique = (ImageView) findViewById(R.id.boutique);
        tabObjets = (TabLayout) findViewById(R.id.tabObjets);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabObjets.setupWithViewPager(viewPager);

        outils = MainActivity.joueur.getOutilsInventaire();
        //Ajout d'e cases vides
        //for(int j = 0 ; j < 48 ; j++)
            //outils.add(new Outil("Case vide", "La case vide ne vous sera pas très utile.",Objet.Type.Décoration, Outil.Portee.Nulle, 0, 0, 0, ""));
        //outils.add(new Outil("Seau d'eau","Le seau d'eau ne contient pas de l'eau, mais plutôt de la Vodka", Objet.Type.Outil, Outil.Portee.Éloignée,6,1,1,"objet_outil_seau_deau", true));
        //outils.add(new Outil("Master-Ball","La Master-Ball est une Poké-Ball utilisée par les meilleurs dresseurs de pokémons dans Pokémons, il faut être un maitre dans l'art pour l'utiliser!", Objet.Type.Outil, Outil.Portee.Éloignée,20,3,1,"objet_outil_masterball", true));
        skins = MainActivity.joueur.getSkinsInventaire();
        //skins.add(new Outil("Pijama","Un pijama rend nos nuits beaucoup plus conforatbles, n'est-ce pas ?", Objet.Type.Skin, Outil.Portee.Nulle, 20, 0, 0, "arthur2_1", true));
        //skins.add(new Outil("Superman","Avec des super pouvoirs aussi puissants que les miens, moi, SuperArthur, je serai inéffrayable!", Objet.Type.Skin, Outil.Portee.Nulle, 40, 0, 0, "arthur7_1", true));
        decorations = MainActivity.joueur.getDecorationsInventaire();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;
        boutique.setOnTouchListener(new View.OnTouchListener()
        {
            float xRepere;
            float yRepere;
            float xCoord;
            float yCoord;
            float Xdiff;
            float Ydiff;

            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        xRepere = event.getRawX();
                        yRepere = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Xdiff <= 20 && Ydiff <= 20 && Xdiff >= -20 && Ydiff >= -20)
                        {
                            view.performClick();
                            startActivity(new Intent(Inventaire.this, Boutique.class));
                            finish();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Xdiff = event.getRawX() - xRepere;
                        Ydiff = event.getRawY() - yRepere;
                        if (Xdiff > 20 || Ydiff > 20 || Xdiff < -20 || Ydiff < -20)
                        {
                            xCoord = event.getRawX() - view.getWidth()/2;
                            yCoord = event.getRawY() - view.getHeight()/(1.2f);
                            if((xCoord + view.getWidth()) < width && (xCoord) > 0)
                            {
                                layoutParams.leftMargin = (int) xCoord;
                            }
                            if((yCoord + view.getHeight()*(1.3f)) < height && (yCoord) > 0)
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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            LinearLayout layoutBarreDOutils = (LinearLayout) findViewById(R.id.layout_barreDOutils);
            ViewGroup.LayoutParams params = layoutBarreDOutils.getLayoutParams();
            params.width = height;
            layoutBarreDOutils.setLayoutParams(params);
        }
    }

    //Méthodes
    public void ajoutInventaire(Outil objetAcheter)
    {
        if(objetAcheter.getType() == Objet.Type.Outil)
        {
            outils.add(objetAcheter);
        }
        else if(objetAcheter.getType() == Objet.Type.Skin)
        {
            skins.add(objetAcheter);
        }
        else if(objetAcheter.getType() == Objet.Type.Décoration)
        {
            skins.add(objetAcheter);
        }
    }

    //custom ArrayAdapter
    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return newInstance(outils);
                case 1:
                    return newInstance(skins);
                case 2:
                    return newInstance(decorations);
                default:
                    return newInstance(new ArrayList<Outil>());
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        //TODO allo
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Outils";
                case 1:
                    return "Skins";
                case 2:
                    return "Décorations";
                default:
                    return "Vide";
            }
        }
    }
}
