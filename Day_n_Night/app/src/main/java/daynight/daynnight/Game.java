package daynight.daynnight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.Shader;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.ObjParser;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-17.
 */

class Game extends GameView{
    private Context mContext;

    private long mHeroID, mTileID, mSID;
    private ArrayList<Long> mBallesID;
    private ArrayList<MovingModel> mBalles;
    private ArrayList<Float> mDirectionsBallesX;
    private ArrayList<Float> mDirectionsBallesY;
    private float xPercentDirectionBalle = 0;
    private float yPercentDirectionBalle = 0;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimerReload;
    private int nbrBallesLancees = 0;
    private Shader texShader;
    private Texture tex;
    private MovingModel perso;
    private Vec3 persoVec;
    private World world;

    public Game(Context context) {
        super(context);

        init(context);
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        mContext = context;
    }


    @Override
    protected void onCreate() {
        world = new World();
        super.UseWorld(world);

        /*joystickTir.joystickCallback(new Joystick.JoystickListener() {
            @Override
            public void onJoystickMoved(float xPercent, float yPercent, int source) throws IOException {
                //Tirs
                xPercentDirectionBalle = xPercent;
                yPercentDirectionBalle = yPercent;
            }
        });*/

        countDownTimer = new CountDownTimer(1000, 500) {
            @Override
            public void onTick(long l) {
                if(xPercentDirectionBalle != 0 && yPercentDirectionBalle != 0){
                    try {
                        makeMrBalle();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFinish() {
                if(nbrBallesLancees >= 10){
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                    }
                    countDownTimerReload = new CountDownTimer(2000,2000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            countDownTimer.start();
                        }
                    };
                    countDownTimerReload.start();
                }else{
                    countDownTimer.start();
                }
            }
        };
        if(countDownTimer != null){
            countDownTimer.start();
        }

/*
        try{
            //Create a tile
            MovingModel tile = ObjParser.Parse(mContext, "models","tuile_cuisine.obj").get(0).toMovingModel();
            tile.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 2));

            MovingModel tile2 = new MovingModel(tile);
            tile2.getPhysics().setSpeed(1.5f);

            StaticModel tile3 = new StaticModel(tile2);

            mTileID = world.addModel(tile);
            mHeroID = world.addModel(tile2);
            world.addModel(tile3);

            MovingModel s = ObjParser.Parse(mContext, "models", "strange.obj").get(0).toMovingModel();
            s.setPhysics(new PhysicsAttributes.MovingModelAttr(50000, 0, 0, 3.5f));
            mSID = world.addModel(s);

            world.Translate(tile, new Vec3(-3, -3, 0));
            world.Translate(tile2, new Vec3(3, 3, 0));
            world.Translate(mSID, new Vec3(-3, 3, 0));

        }catch(IOException ex){
            Log.e("Models creation", ex.getMessage());
        }
        */
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    @Override
    protected void onDrawFrame(World world) {

        int temp=0;
        //world.getModel()
        for(Long monsieurMovingModelID : mBallesID){
            world.Move(monsieurMovingModelID, new Vec3(mDirectionsBallesX.get(temp), mDirectionsBallesY.get(temp), 0.f), getElapsedFrameTime());
            temp++;
            //if(positionBalle == positionAutreShit) {
            //  destroyMrBalle(temp, world);
            // }

        }

/*
        world.Move(mTileID, new Vec3(0.1f, 0.8f, 0.f), getElapsedFrameTime());
        world.Move(mHeroID, new Vec3(0.1f, -0.8f, 0.f), getElapsedFrameTime());
        world.Move(mSID, new Vec3(-1, 0, 0), getElapsedFrameTime());
        */
    }

    public void makeMrBalle() throws IOException {
        MovingModel bullet = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
        bullet.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
        mBalles.add(bullet);
        mDirectionsBallesX.add(this.xPercentDirectionBalle);
        mDirectionsBallesY.add(this.yPercentDirectionBalle);
        nbrBallesLancees++;
        bullet.AssociateShader(texShader);
        bullet.setTexture(tex);
        mBallesID.add(bullet.getID());
    }

    public void destroyMrBalle(int i, World world){
        world.removeModel(mBallesID.get(i));
        mBalles.remove(i);
        mDirectionsBallesX.remove(i);
        mDirectionsBallesY.remove(i);
        mBallesID.remove(i);
    }

    public void movePerso(Vec3 vector){
        world.Move(perso.getID() ,persoVec,getElapsedFrameTime());
    }
}
