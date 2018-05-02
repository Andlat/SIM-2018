package daynight.daynnight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static daynight.daynnight.MainActivity.joueur;

/**
 * Created by Antoine Mascolo on 2018-04-17.
 */

public class PopupRecompenses extends Activity {

    private Button ramasser;
    private String ok;
    private ArrayList<Outil> outilsLegend;
    private ArrayList<Outil> outilsRare;
    private ArrayList<Outil> outilsCommon;
    private ArrayList<Outil> skinsLegend;
    private ArrayList<Outil> skinsRare;
    private ArrayList<Outil> skinsCommon;
    private Random rand;
    private int biscuit;
    private int generate;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recompense);

        //Parametre pour que l'activity ne prenne pas tout l'écran
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.75), (int) (height * .5));

        ok = "non";
        ramasser = findViewById(R.id.ramasser);
        TextView recompense = (TextView) findViewById(R.id.recompenseMessage);


        //classe les outils selon leur rarete
        for(int i = 0; i < MainActivity.joueur.getOutilsBoutique().size(); i++){
            int rarete = MainActivity.joueur.getOutilsBoutique().get(i).getRarete();

            if(rarete == 1) outilsCommon.add(MainActivity.joueur.getOutilsBoutique().get(i));
            else if(rarete == 2)outilsRare.add(MainActivity.joueur.getOutilsBoutique().get(i));
            else if(rarete == 3)outilsLegend.add(MainActivity.joueur.getOutilsBoutique().get(i));
        }
        //classe les skins selon leur rarete
        for(int i = 0; i < MainActivity.joueur.getSkinsBoutique().size(); i++){
            int rarete = MainActivity.joueur.getSkinsBoutique().get(i).getRarete();

            if(rarete == 1) skinsCommon.add(MainActivity.joueur.getSkinsBoutique().get(i));
            else if(rarete == 2)skinsRare.add(MainActivity.joueur.getSkinsBoutique().get(i));
            else if(rarete == 3)skinsLegend.add(MainActivity.joueur.getSkinsBoutique().get(i));
        }

        generate = rand.nextInt(101);
        if(generate < 60){
            //donne entre 5 et 25 biscuits
            biscuit = rand.nextInt(21) + 5;
            MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() + biscuit);
            recompense.setText(biscuit + "biscuits!");

        }else{
            //Slection la rarete de l'item
            generate = rand.nextInt(101);

            if(generate < 70){
                //Donne un item commun
                generate = rand.nextInt(2);

                if(generate == 0){
                    //skin
                    generate = rand.nextInt(skinsCommon.size());
                    recompense.setText(skinsCommon.get(generate).getNom());
                    //TODO ajouter à l'inventaire et retirer de la boutique
                }else{
                    //outil
                    generate = rand.nextInt(outilsCommon.size());
                    recompense.setText(outilsCommon.get(generate).getNom());
                    //TODO ajouter à l'inventaire et retirer de la boutique
                }


            }else if(generate < 90){
                //donne un item rare
                generate = rand.nextInt(2);

                if(generate == 0){
                    //skin
                    generate = rand.nextInt(skinsRare.size());
                    recompense.setText(skinsRare.get(generate).getNom());
                    //TODO ajouter à l'inventaire et retirer de la boutique
                }else{
                    //outil
                    generate = rand.nextInt(outilsRare.size());
                    recompense.setText(outilsRare.get(generate).getNom());
                    //TODO ajouter à l'inventaire et retirer de la boutique
                }

            }else{
                //donne un item legendaire
                generate = rand.nextInt(2);

                if(generate == 0){
                    //skin
                    generate = rand.nextInt(skinsLegend.size());
                    recompense.setText(skinsLegend.get(generate).getNom());
                    //TODO ajouter à l'inventaire et retirer de la boutique
                }else{
                    //outil
                    generate = rand.nextInt(outilsLegend.size());
                    recompense.setText(outilsLegend.get(generate).getNom());
                    //TODO ajouter à l'inventaire et retirer de la boutique
                }
            }

        }




        ramasser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //Retourne le code "ok" a la map activity pour que le poi désactive
                ok = "ok";
                Intent intent = new Intent(PopupRecompenses.this, MapActivity.class);
                intent.putExtra("RECUPERER", ok);
                setResult(RESULT_OK, intent);
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
}
