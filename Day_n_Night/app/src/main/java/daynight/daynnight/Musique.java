package daynight.daynnight;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by anthonymartin on 18-03-26.
 */

public class Musique
{
    MediaPlayer backgroundMusique;

    Musique()
    {

    }

    //Musique d'arriere plan
    protected void startMusic(Context context){
        backgroundMusique = MediaPlayer.create(context, R.raw.musiquebackground);
        backgroundMusique.setLooping(true);
        backgroundMusique.start();

    }

    /*
    protected void onPause(){
        super.onPause();
        backgroundMusique.release();
        finish();
    }
    */

}
