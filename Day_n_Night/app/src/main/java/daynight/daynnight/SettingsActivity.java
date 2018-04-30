package daynight.daynnight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static daynight.daynnight.MainActivity.joueur;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

/*
Crédits musique :

Mystery by GoSoundtrack http://www.gosoundtrack.com/
Creative Commons — Attribution 4.0 International — CC BY 4.0
https://creativecommons.org/licenses/...
Music promoted by Audio Library https://youtu.be/8TKy9bzrk24
*/
