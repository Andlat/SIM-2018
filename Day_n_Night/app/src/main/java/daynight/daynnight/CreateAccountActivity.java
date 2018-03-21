package daynight.daynnight;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class CreateAccountActivity extends AppCompatActivity {
    Button boutonOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        boutonOk = (Button)findViewById(R.id.boutonOk);
        //Changement couleur bouton lorsque touché
        boutonOk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN ) {
                    boutonOk.setBackground(getDrawable(R.drawable.bouton1_2));
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    boutonOk.setBackground(getDrawable(R.drawable.bouton1_1));
                }
                return false;
            }
        });

        boutonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boutonOk.setBackground(getDrawable(R.drawable.bouton1_2));
            }
        });
    }
}
