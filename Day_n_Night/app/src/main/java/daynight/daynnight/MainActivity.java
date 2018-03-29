package daynight.daynnight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Musique d'arriere plan
        /*backgroundMusique = MediaPlayer.create(MainActivity.this, R.raw.musiquebackground);
        backgroundMusique.setLooping(true);
        backgroundMusique.start();*/
        Musique musique = new Musique();
        musique.startMusic(getApplicationContext());


        //Bouton jeu de jour
        Button buttonDay = (Button) findViewById(R.id.jourButton);
        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        //Bouton jeu de nuit
        Button buttonNight = (Button) findViewById(R.id.nuitButton);
        buttonNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ceci est juste un test pour le bouton pause
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
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
                startActivity(new Intent(MainActivity.this, Inventaire.class));
            }
        });

        //Bouton pour les badges
        Button leSebNouton2 = (Button) findViewById(R.id.leSebBouton2);
        leSebNouton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, ListeBadges.class));
            }
        });

        //Bouton pour réglages
        Button boutonReglages = (Button) findViewById(R.id.boutonReglages);
        boutonReglages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    //Arrete la musique lorsque l'application est fermée
    @Override
    protected void onPause(){
        super.onPause();
        //backgroundMusique.release();
    }

}
