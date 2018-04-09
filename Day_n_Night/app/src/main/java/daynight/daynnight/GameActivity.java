package daynight.daynnight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity{
    private Button buttonRecommencer;
    private Button buttonMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        buttonRecommencer = (Button)findViewById(R.id.buttonRecommencer);
        //Changement couleur bouton lorsque touché
        buttonRecommencer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        buttonMenu = (Button)findViewById(R.id.buttonMenu);
        //Changement couleur bouton lorsque touché
        buttonMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN ) {
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                }
                return false;
            }
        });
    }
}
