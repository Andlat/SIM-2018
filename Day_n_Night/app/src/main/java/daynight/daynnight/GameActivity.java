package daynight.daynnight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import daynight.daynnight.engine.math.Vec3;


public class GameActivity extends AppCompatActivity implements Joystick.JoystickListener{
    private Button buttonRecommencer;
    private Button buttonMenu;
    private Joystick joystickTir;
    private Joystick joystickPerso;
    private Vec3 persoVec;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = (Game) findViewById(R.id.game1);
        joystickTir = (Joystick) findViewById(R.id.joystickTir);
        joystickPerso = (Joystick) findViewById(R.id.joystickPerso);
        joystickPerso.setColor(105,105,105);


    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) throws IOException {
        switch(source){
            case R.id.joystickPerso:

                persoVec.x(xPercent);
                persoVec.y(yPercent * -1);
                //game.movePerso(persoVec);
                break;

            case R.id.joystickTir:

                break;

        }

    }
}
