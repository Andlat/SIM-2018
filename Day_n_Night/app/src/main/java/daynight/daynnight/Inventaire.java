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
import static daynight.daynnight.MainActivity.SurChangementActivity;
import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.onPause;

public class Inventaire extends AppCompatActivity
{
    //Variables
    static Inventaire inventaire;
    ImageView boutique;

    TabLayout tabObjets;
    ViewPager viewPager;

    Fragment outilsFragment = newInstance(MainActivity.joueur.getOutilsInventaire());
    Fragment skinsFragment = newInstance(MainActivity.joueur.getSkinsInventaire());
    Fragment decorationsFragment = newInstance(MainActivity.joueur.getDecorationsInventaire());

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inventaire);
        inventaire = this;

        //Attribuer
        boutique = (ImageView) findViewById(R.id.boutique);
        tabObjets = (TabLayout) findViewById(R.id.tabObjets);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabObjets.setupWithViewPager(viewPager);

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
                            SurChangementActivity = true;
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

    @Override
    protected void onPause()
    {
        super.onPause();
        //onPause = false;
    }

    @Override
    protected void onStop()
    {
        if(SurChangementActivity)//TODO le problème est ici
        {
            MainActivity.musiqueDeFond.pause();
            MainActivity.ma.sauvegardeJoueur(joueur);
        }
        super.onStop();
    }
    @Override
    protected void onResume()
    {
        MainActivity.musiqueDeFond.start();
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SurChangementActivity = false;
    }
    //Méthodes

    //custom ArrayAdapter
    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return outilsFragment;
                case 1:
                    return skinsFragment;
                case 2:
                    return decorationsFragment;
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
