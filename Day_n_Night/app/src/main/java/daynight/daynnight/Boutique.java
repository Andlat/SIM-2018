package daynight.daynnight;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import java.util.ArrayList;

import static daynight.daynnight.ListeObjets.newInstance;

public class Boutique extends AppCompatActivity
{
    //Créer
    Button retour;
    Button boutonOutils;
    Button boutonSkins;
    Button boutonDecorations;
    ViewPager viewPager;

    Fragment outilsFragment = newInstance(MainActivity.joueur.getOutilsBoutique());
    Fragment skinsFragment = newInstance(MainActivity.joueur.getSkinsBoutique());
    Fragment decorationsFragment = newInstance(MainActivity.joueur.getDecorationsBoutique());

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_boutique);



        //Attribuer
        retour = (Button) findViewById(R.id.retour);
        boutonOutils = (Button) findViewById(R.id.outils);
        boutonSkins = (Button) findViewById(R.id.skins);
        boutonDecorations = (Button) findViewById(R.id.decos);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

        //outils = MainActivity.joueur.getOutilsBoutique();
        //skins = MainActivity.joueur.getSkinsBoutique();
        //decorations = MainActivity.joueur.getDecorationsBoutique();


        boutonOutils.setSelected(true);
        boutonOutils.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
        boutonOutils.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tabBoutonsSelection(view, boutonSkins, boutonDecorations);
                viewPager.setCurrentItem(0);
            }
        });
        boutonSkins.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tabBoutonsSelection(view, boutonOutils, boutonDecorations);
                viewPager.setCurrentItem(1);
            }
        });
        boutonDecorations.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tabBoutonsSelection(view, boutonSkins, boutonOutils);
                viewPager.setCurrentItem(2);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position)
            {
                switch(position)
                {
                    case 0:
                        tabBoutonsSelection(boutonOutils, boutonSkins, boutonDecorations);
                        break;
                    case 1:
                        tabBoutonsSelection(boutonSkins, boutonOutils, boutonDecorations);
                        break;
                    case 2:
                        tabBoutonsSelection(boutonDecorations, boutonSkins, boutonOutils);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        retour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
                startActivity(new Intent(Boutique.this, Inventaire.class));
            }
        });
    }

    //Méthodes
    private void tabBoutonsSelection(View selection, View normal, View normal2)
    {
        selection.setSelected(true);
        normal.setSelected(false);
        normal2.setSelected(false);
        selection.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
        normal.setTranslationX(0);
        normal2.setTranslationX(0);
    }
    /*public void acheterObjet(int position, Objet.Type type)
    {
        //TODO
        if(type == Objet.Type.Outil)
        {
            outils.remove(position);
        }
        else if(type == Objet.Type.Skin)
        {
            skins.remove(position);
        }
        else if(type == Objet.Type.Décoration)
        {
            skins.remove(position);
        }
    }*/


    //custom ArrayAdapters
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
