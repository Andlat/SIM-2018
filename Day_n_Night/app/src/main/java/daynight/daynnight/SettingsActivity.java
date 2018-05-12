package daynight.daynnight;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import static daynight.daynnight.MainActivity.SurChangementActivity;
import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.onPause;

public class SettingsActivity extends Activity {

    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Parametre pour que l'activity ne prenne pas tout l'écran
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //Les proportions change selon l'orientation de l'écran
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().setLayout((int) (width * 0.85), (int) (height * 0.75));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setLayout((int) (width * 0.75), (int) (height * 0.85));
        }

        Button ok = (Button) findViewById(R.id.buttonOK);
        Button music = (Button) findViewById(R.id.buttonMusic);
        Button fr = (Button) findViewById(R.id.buttonFrancais);
        Button eng = (Button) findViewById(R.id.buttonAnglais);
        Button restart = (Button) findViewById(R.id.buttonRecommencer);

        music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(MainActivity.musiqueDeFond.isPlaying()){
                    MainActivity.musiqueDeFond.pause();
                    MainActivity.joueur.setMusique(false);
                }else{
                    //MainActivity.musiqueDeFond.start();
                    MainActivity.joueur.setMusique(true);
                }

            }
        });

        fr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setLocale("fr");
                MainActivity.joueur.setLangue("fr");
            }
        });

        eng.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setLocale("en");
                MainActivity.joueur.setLangue("en");
            }
        });

        restart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getApplicationContext().deleteFile(MainActivity.fichierJoueur.getName());
            }
        });

        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
    @Override
    protected void onStop()
    {
        if(SurChangementActivity)
        {
            /*MainActivity.musiqueDeFond.pause();
            MainActivity.ma.sauvegardeJoueur(joueur);*/
        }
        super.onStop();
    }
    @Override
    protected void onResume()
    {
        //MainActivity.musiqueDeFond.start();
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SurChangementActivity = false;
    }

    //Change la langue de l'application
    //Fonction de Rubin Nellikunnathu sur https://stackoverflow.com/questions/34573201/change-languages-in-app-via-strings-xml
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SettingsActivity.class);
        startActivity(refresh);
    }
}

/*
Crédits musique :

Mystery by GoSoundtrack http://www.gosoundtrack.com/
Creative Commons — Attribution 4.0 International — CC BY 4.0
https://creativecommons.org/licenses/...
Music promoted by Audio Library https://youtu.be/8TKy9bzrk24
*/
