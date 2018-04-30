package daynight.daynnight;

import android.app.Activity;
<<<<<<< HEAD
=======
import android.app.Fragment;
import android.content.Intent;
>>>>>>> 03a2ee4bb73c876278f8f44a3b324e401b51e25b
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
    private String ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recompense);

        ok = "non";
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.75), (int) (height * .5));

        ramasser = findViewById(R.id.ramasser);
        ramasser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
