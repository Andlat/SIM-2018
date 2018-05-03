package daynight.daynnight;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import daynight.daynnight.engine.math.Vec3;

import static daynight.daynnight.MainActivity.joueur;
import static daynight.daynnight.MainActivity.onPause;


public class GameActivity extends AppCompatActivity implements Joystick.JoystickListener{
    private Button buttonRecommencer;
    private Button buttonMenu;
    private Joystick joystickTir;
    private Joystick joystickPerso;
    private Vec3 persoVec;
    private Game game;
    private Vec3 balleVec;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = (Game) findViewById(R.id.game1);
        joystickTir = (Joystick) findViewById(R.id.joystickTir);
        joystickTir.setColor(105,105, 105);
        joystickPerso = (Joystick) findViewById(R.id.joystickPerso);
        joystickPerso.setColor(105,105,105);

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
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) throws IOException {
        switch(source){
            case R.id.joystickPerso:
                persoVec.x(xPercent);
                persoVec.y(-yPercent);
                //game.movePerso(persoVec);
                break;

            case R.id.joystickTir:
                balleVec.x(xPercent);
                balleVec.y(-yPercent);
                break;
        }
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
