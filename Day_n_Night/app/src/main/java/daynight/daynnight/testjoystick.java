package daynight.daynnight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class testjoystick extends AppCompatActivity implements Joystick.JoystickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testjoystick);

        Joystick jstest = (Joystick) findViewById(R.id.joystick);

    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        TextView textX = (TextView) findViewById(R.id.textViewX);
        TextView textY = (TextView) findViewById(R.id.textViewY);
        String tex = "X: " + xPercent + "%";
        String tey = "Y: " + yPercent + "%";

        textX.setText(tex);
        textY.setText(tey);
    }
}
