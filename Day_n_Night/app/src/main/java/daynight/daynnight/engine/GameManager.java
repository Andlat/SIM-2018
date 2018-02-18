package daynight.daynnight.engine;

import android.view.View;

import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-05.
 */

abstract public class GameManager {
    private boolean mIsPhysicsOn = false;

    abstract public void onCreate();

    abstract public void onChangeSize(int width, int height);

    abstract public void onDrawFrame();//Game Loop

    public void Quit(){

    }

    public void setQuitOnPressed(View view){

    }

    public void activatePhysics(boolean activate){ mIsPhysicsOn = activate; }
    public boolean isPhysicsActivated(){ return mIsPhysicsOn; }


}
