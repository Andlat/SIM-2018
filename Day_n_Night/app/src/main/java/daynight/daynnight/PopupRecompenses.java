package daynight.daynnight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static daynight.daynnight.MainActivity.SurChangementActivity;
import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.onPause;

/**
 * Created by Antoine Mascolo on 2018-04-17.
 */

public class PopupRecompenses extends Activity {

    private Button ramasser;
    ImageView image;
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
    private int position;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recompense);

        outilsLegend = new ArrayList<Outil>();
        outilsRare = new ArrayList<Outil>();
        outilsCommon = new ArrayList<Outil>();
        skinsLegend = new ArrayList<Outil>();
        skinsRare = new ArrayList<Outil>();
        skinsCommon = new ArrayList<Outil>();
        rand = new Random();

        //Parametre pour que l'activity ne prenne pas tout l'écran
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.75), (int) (height * .5));

        ok = "non";
        ramasser = findViewById(R.id.ramasser);
        TextView recompense = (TextView) findViewById(R.id.recompenseMessage);
        image = findViewById(R.id.recImage);


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
            recompense.setText(biscuit + " biscuits!");

        }else{
            //Slection la rarete de l'item
            generate = rand.nextInt(101);

            if(generate < 70){
                //Donne un item commun
                generate = rand.nextInt(2);

                if(generate == 0){
                    //skin
                    if(skinsCommon.size() >0) {
                        generate = rand.nextInt(skinsCommon.size());
                        skinsCommon.get(generate).setAcquis(true);
                        recompense.setText(skinsCommon.get(generate).getNom());
                        image.setImageResource(getResources().getIdentifier(skinsCommon.get(generate).getImageDrawableString(), "drawable", getPackageName()));
                        for(int k =0; k< MainActivity.joueur.getSkinsBoutique().size(); k++){
                            if(skinsCommon.get(generate).getId() == MainActivity.joueur.getSkinsBoutique().get(k).getId()){
                                position = k;
                                break;
                            }
                        }
                        MainActivity.joueur.getSkinsInventaire().add(skinsCommon.get(generate));
                        MainActivity.joueur.getSkinsBoutique().set(position, new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));

                    }else{
                        //si il n'y a rien a gagner, donne des biscuits
                        biscuit = rand.nextInt(21) + 5;
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() + biscuit);
                        recompense.setText(biscuit + " biscuits!");
                    }
                }else{
                    if(outilsCommon.size() >0) {
                        //outil
                        generate = rand.nextInt(outilsCommon.size());
                        outilsCommon.get(generate).setAcquis(true);
                        recompense.setText(outilsCommon.get(generate).getNom());
                        image.setImageResource(getResources().getIdentifier(outilsCommon.get(generate).getImageDrawableString(), "drawable", getPackageName()));
                        for(int k =0; k< MainActivity.joueur.getOutilsBoutique().size(); k++){
                            if(outilsCommon.get(generate).getId() == MainActivity.joueur.getOutilsBoutique().get(k).getId()){
                                position = k;
                                break;
                            }
                        }
                        MainActivity.joueur.getOutilsInventaire().add(outilsCommon.get(generate));
                        MainActivity.joueur.getOutilsBoutique().set(position, new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));

                    }else{
                        //si il n'y a rien a gagner, donne des biscuits
                        biscuit = rand.nextInt(21) + 5;
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() + biscuit);
                        recompense.setText(biscuit + " biscuits!");
                    }
                }


            }else if(generate < 90){
                //donne un item rare
                generate = rand.nextInt(2);

                if(generate == 0){
                    if(skinsRare.size() > 0) {
                        //skin
                        generate = rand.nextInt(skinsRare.size());
                        skinsRare.get(generate).setAcquis(true);
                        recompense.setText(skinsRare.get(generate).getNom());
                        image.setImageResource(getResources().getIdentifier(skinsRare.get(generate).getImageDrawableString(), "drawable", getPackageName()));
                        for(int k =0; k< MainActivity.joueur.getSkinsBoutique().size(); k++){
                            if(skinsRare.get(generate).getId() == MainActivity.joueur.getSkinsBoutique().get(k).getId()){
                                position = k;
                                break;
                            }
                        }
                        MainActivity.joueur.getSkinsInventaire().add(skinsRare.get(generate));
                        MainActivity.joueur.getSkinsBoutique().set(position, new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));

                    }else{
                        //si il n'y a rien a gagner, donne des biscuits
                        biscuit = rand.nextInt(21) + 5;
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() + biscuit);
                        recompense.setText(biscuit + " biscuits!");
                    }

                }else{
                    if(outilsRare.size() > 0) {
                        //outil
                        generate = rand.nextInt(outilsRare.size());
                        outilsRare.get(generate).setAcquis(true);
                        recompense.setText(outilsRare.get(generate).getNom());
                        image.setImageResource(getResources().getIdentifier(outilsRare.get(generate).getImageDrawableString(), "drawable", getPackageName()));
                        for(int k =0; k< MainActivity.joueur.getOutilsBoutique().size(); k++){
                            if(outilsRare.get(generate).getId() == MainActivity.joueur.getOutilsBoutique().get(k).getId()){
                                position = k;
                                break;
                            }
                        }
                        MainActivity.joueur.getOutilsInventaire().add(outilsRare.get(generate));
                        MainActivity.joueur.getOutilsBoutique().set(position, new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));

                    }else{
                        //si il n'y a rien a gagner, donne des biscuits
                        biscuit = rand.nextInt(21) + 5;
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() + biscuit);
                        recompense.setText(biscuit + " biscuits!");
                    }
                }

            }else{
                //donne un item legendaire
                generate = rand.nextInt(2);

                if(generate == 0){
                    if(skinsLegend.size() >0) {
                        //skin
                        generate = rand.nextInt(skinsLegend.size());
                        skinsLegend.get(generate).setAcquis(true);
                        recompense.setText(skinsLegend.get(generate).getNom());
                        image.setImageResource(getResources().getIdentifier(skinsLegend.get(generate).getImageDrawableString(), "drawable", getPackageName()));
                        for(int k =0; k< MainActivity.joueur.getSkinsBoutique().size(); k++){
                            if(skinsLegend.get(generate).getId() == MainActivity.joueur.getSkinsBoutique().get(k).getId()){
                                position = k;
                                break;
                            }
                        }
                        MainActivity.joueur.getSkinsInventaire().add(skinsLegend.get(generate));
                        MainActivity.joueur.getSkinsBoutique().set(position, new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));

                    }else{
                        //si il n'y a rien a gagner, donne des biscuits
                        biscuit = rand.nextInt(21) + 5;
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() + biscuit);
                        recompense.setText(biscuit + " biscuits!");
                    }

                }else{
                    if(outilsLegend.size() >0) {
                        //outil
                        generate = rand.nextInt(outilsLegend.size());
                        outilsLegend.get(generate).setAcquis(true);
                        recompense.setText(outilsLegend.get(generate).getNom());
                        image.setImageResource(getResources().getIdentifier(outilsLegend.get(generate).getImageDrawableString(), "drawable", getPackageName()));
                        for(int k =0; k< MainActivity.joueur.getOutilsBoutique().size(); k++){
                            if(outilsLegend.get(generate).getId() == MainActivity.joueur.getOutilsBoutique().get(k).getId()){
                                position = k;
                                break;
                            }
                        }
                        MainActivity.joueur.getOutilsInventaire().add(outilsLegend.get(generate));
                        MainActivity.joueur.getOutilsBoutique().set(position, new Outil(666, "Case vide", "La case vide ne vous sera pas très utile.", Objet.Type.Décoration, 0, Outil.Portee.Nulle, 0, 0, 0, 0f, "", true));

                    }else{
                        //si il n'y a rien a gagner, donne des biscuits
                        biscuit = rand.nextInt(21) + 5;
                        MainActivity.joueur.setBiscuits(MainActivity.joueur.getBiscuits() + biscuit);
                        recompense.setText(biscuit + " biscuits!");
                    }

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
}
