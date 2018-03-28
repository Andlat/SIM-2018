package daynight.daynnight;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    ArrayList<Outil> outils;
    ArrayList<Outil> skins;
    ArrayList<Outil> decorations;

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

        outils = new ArrayList<>();
        outils.add(new Outil("Seau d'eau","Le seau d'eau ne contient pas de l'eau, mais plutôt de la Vodka", Objet.Type.Outil, Outil.Portee.Éloignée,6,1,1,"objet_outil_seau_deau"));
        outils.add(new Outil("Master-Ball","La Master-Ball est une Poké-Ball utilisée par les meilleurs dresseurs de pokémons dans Pokémons, il faut être un maitre dans l'art pour l'utiliser!", Objet.Type.Outil, Outil.Portee.Éloignée,20,3,1,"objet_outil_masterball"));
        skins = new ArrayList<>();
        skins.add(new Outil("Pijama","Un pijama rend nos nuits beaucoup plus conforatbles, n'est-ce pas ?", Objet.Type.Skin, Outil.Portee.Nulle, 20, 0, 0, "arthur2_1"));
        skins.add(new Outil("Superman","Avec des super pouvoirs aussi puissants que les miens, moi, SuperArthur, je serai inéffrayable!", Objet.Type.Skin, Outil.Portee.Nulle, 40, 0, 0, "arthur7_1"));
        decorations = new ArrayList<>();

        boutonOutils.setSelected(true);
        boutonOutils.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
        boutonOutils.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.setSelected(true);
                boutonSkins.setSelected(false);
                boutonDecorations.setSelected(false);
                view.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
                boutonSkins.setTranslationX(0);
                boutonDecorations.setTranslationX(0);

                viewPager.setCurrentItem(0);
            }
        });
        boutonSkins.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.setSelected(true);
                boutonOutils.setSelected(false);
                boutonDecorations.setSelected(false);
                view.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
                boutonOutils.setTranslationX(0);
                boutonDecorations.setTranslationX(0);

                viewPager.setCurrentItem(1);
            }
        });
        boutonDecorations.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.setSelected(true);
                boutonSkins.setSelected(false);
                boutonOutils.setSelected(false);
                view.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
                boutonSkins.setTranslationX(0);
                boutonOutils.setTranslationX(0);

                viewPager.setCurrentItem(2);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                switch(position)
                {
                    case 0:
                        boutonOutils.setSelected(true);
                        boutonSkins.setSelected(false);
                        boutonDecorations.setSelected(false);
                        boutonOutils.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
                        boutonSkins.setTranslationX(0);
                        boutonDecorations.setTranslationX(0);
                        break;
                    case 1:
                        boutonSkins.setSelected(true);
                        boutonOutils.setSelected(false);
                        boutonDecorations.setSelected(false);
                        boutonSkins.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
                        boutonOutils.setTranslationX(0);
                        boutonDecorations.setTranslationX(0);
                        break;
                    case 2:
                        boutonDecorations.setSelected(true);
                        boutonSkins.setSelected(false);
                        boutonOutils.setSelected(false);
                        boutonDecorations.setTranslationX(getResources().getDimension(R.dimen.translation_bouton_tab));
                        boutonSkins.setTranslationX(0);
                        boutonOutils.setTranslationX(0);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        retour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
    //custom ArrayAdapters
    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return newInstance(outils, false);
                case 1:
                    return newInstance(skins, false);
                case 2:
                    return newInstance(decorations, false);
                default:
                    return newInstance(new ArrayList<Outil>(), false);
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
