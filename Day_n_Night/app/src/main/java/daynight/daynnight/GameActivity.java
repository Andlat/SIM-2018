package daynight.daynnight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {
    Button buttonRecommencer;
    Button buttonMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        buttonRecommencer = (Button)findViewById(R.id.buttonRecommencer);
        //Changement couleur bouton lorsque touché
        buttonRecommencer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN ) {
                    buttonRecommencer.setBackground(getDrawable(R.drawable.bouton4_2));
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonRecommencer.setBackground(getDrawable(R.drawable.bouton4_1));
                }
                return false;
            }
        });

        buttonMenu = (Button)findViewById(R.id.buttonMenu);
        //Changement couleur bouton lorsque touché
        buttonMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN ) {
                    buttonMenu.setBackground(getDrawable(R.drawable.bouton4_2));
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonMenu.setBackground(getDrawable(R.drawable.bouton4_1));
                }
                return false;
            }
        });
    }
}
