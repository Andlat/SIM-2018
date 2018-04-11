package daynight.daynnight;

import android.content.Intent;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity
{
    private GLSurfaceView mGLSurface;

    public static boolean onPause = false;
    public static boolean SurChangementActivity = false;

    int temps;
    public static MediaPlayer MusiqueDeFond;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //getIntent().setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        setContentView(R.layout.activity_main);

        //Musique d'arriere plan
        MusiqueDeFond = MediaPlayer.create(MainActivity.this, R.raw.musiquebackground);
        MusiqueDeFond.setLooping(true);
        MusiqueDeFond.start();

        //Bouton jeu de jour
        Button buttonDay = (Button) findViewById(R.id.jourButton);
        buttonDay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        //Bouton jeu de nuit
        Button buttonNight = (Button) findViewById(R.id.nuitButton);
        buttonNight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SurChangementActivity = true;
                //Ceci est juste un test pour le bouton pause
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
                startActivity(intent);
            }
        });

        //Bouton pour l'inventaire
        Button leSebBouton = (Button) findViewById(R.id.leSebBouton);
        leSebBouton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, Inventaire.class);
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
                startActivity(intent);
            }
        });

        //Bouton pour les badges
        Button leSebNouton2 = (Button) findViewById(R.id.leSebBouton2);
        leSebNouton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, ListeBadges.class);
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
                startActivity(intent);
            }
        });

        //Bouton pour réglages
        Button boutonReglages = (Button) findViewById(R.id.boutonReglages);
        boutonReglages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SurChangementActivity = true;
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                //temps = backgroundMusique.getCurrentPosition();
                //intent.putExtra("TEMPS", temps);
                startActivity(intent);
            }
        });
    }

    //Arrête la musique lorsque l'application ferme et commence quand elle ouvre
    @Override
    protected void onPause()
    {
        super.onPause();

        //backgroundMusique.release();
        backgroundMusique.release();

        onPause = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MusiqueDeFond.start();
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        if(onPause && !SurChangementActivity)
        {
            MusiqueDeFond.pause();
            onPause = false;

        }
    }

}
