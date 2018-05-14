package daynight.daynnight;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Util;

import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.SurChangementActivity;


public class GameActivity extends AppCompatActivity implements Joystick.JoystickListener{
    private Button buttonRecommencer;
    private Button buttonMenu;
    private ImageButton buttonPause;
    private Joystick joystickTir;
    private Joystick joystickPerso;
    private Vec3 persoVec = new Vec3();
    private Game game;
    private Vec3 balleVec = new Vec3();
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = (Game) findViewById(R.id.game1);
        joystickTir = (Joystick) findViewById(R.id.joystickTir);
        joystickTir.setZOrderOnTop(true);
        joystickTir.setColor(105,105, 105);
        joystickPerso = (Joystick) findViewById(R.id.joystickPerso);
        joystickPerso.setZOrderOnTop(true);
        joystickPerso.setColor(105,105,105);

        buttonPause = (ImageButton) findViewById(R.id.pauseNuit);
        buttonPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(GameActivity.this, PopupPause.class));
                SurChangementActivity = true;
            }
        });
/*
        countDownTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if(balleVec == new Vec3(0,0,0)){
                    try{
                        game.makeMrBalle();
                    }catch(IOException i){

                    }
                }
                start();
            }
        }.start();


*/
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) throws IOException {
        Arthur arthur = game.getArthur();
        if(arthur != null) {
            switch(source){
                case R.id.joystickPerso:
                    persoVec.x(xPercent);
                    persoVec.y(yPercent);


                    arthur.setDirection(persoVec);
                    arthur.checkSwitch(xPercent);

                    if (persoVec.isEmpty())
                        arthur.Stay();
                    else
                        arthur.Walk();

                    break;

                case R.id.joystickTir:
                    balleVec.x(xPercent);
                    balleVec.y(yPercent);

                    arthur.setToolDir(new Vec3(xPercent, yPercent, 0));

                    break;
            }
        }
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
        MainActivity.musiqueDeFond.start();
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SurChangementActivity = false;
    }
}
