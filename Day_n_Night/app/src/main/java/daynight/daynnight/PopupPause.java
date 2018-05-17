package daynight.daynnight;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import static daynight.daynnight.MainActivity.SurChangementActivity;
import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.onPause;

public class PopupPause extends Activity {
    private ImageButton play;
    private Button badges;
    private Button inventaire;
    private Button menu;
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_pause);

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

        play = (ImageButton) findViewById(R.id.playPauseButton);
        badges = (Button) findViewById(R.id.badgePauseButton);
        inventaire = (Button) findViewById(R.id.inventairePauseButton);
        menu = (Button) findViewById(R.id.menuPauseButton);
        settings = (Button) findViewById(R.id.settingsPauseButton);

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        badges.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PopupPause.this, ListeBadges.class));
                SurChangementActivity = true;

            }
        });

        inventaire.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PopupPause.this, Inventaire.class));
                SurChangementActivity = true;
            }
        });

        menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PopupPause.this, MainActivity.class));
                SurChangementActivity = true;
            }
        });

        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PopupPause.this, SettingsActivity.class));
                SurChangementActivity = true;
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
