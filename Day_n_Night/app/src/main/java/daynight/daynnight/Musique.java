package daynight.daynnight;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.SeekBar;

import java.util.logging.Handler;

/**
 * Created by anthonymartin on 18-03-26.
 */

public class Musique extends Service{
    private static final String TAG = null;
    MediaPlayer player;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();


        player = MediaPlayer.create(this, R.raw.idil);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {


        player.start();

        return 1;
    }

    public void onStart(Intent intent, int startId) {
        // TODO



    }
    public IBinder onUnBind(Intent arg0) {
        // TODO Auto-generated method stub

        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {

        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
    /*
    SeekBar seekBar;
    MediaPlayer backgroundMusique;
    Handler handler;
    Runnable runnable;
    Musique()
    {

    }
    //Musique d'arriere plan
    protected void startMusic(Context context){
        backgroundMusique = MediaPlayer.create(context, R.raw.musiquebackground);
        backgroundMusique.setLooping(true);
        backgroundMusique.start();
    }
    protected void onPause(){
        super.onPause();
        backgroundMusique.release();
        finish();
    }
    */

}
