package daynight.daynnight;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import static daynight.daynnight.MainActivity.joueur;

/**
 * Created by Antoine Mascolo on 2018-04-17.
 */

public class PopupRecompenses extends Activity {

    private Button ramasser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recompense);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.75), (int) (height * .5));

        ramasser = findViewById(R.id.ramasser);
        ramasser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
