package daynight.daynnight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Coord;

/**
 * Created by andlat on 2018-02-17.
 */

class Game extends GameView{
    private Context mContext;

    private Arthur mArthur;
    private long mArthurID;

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
        World world = new World();
        super.UseWorld(world);

        mArthur = new Arthur(mContext);
        mArthurID = world.addModel(mArthur.getModel());
        world.Translate(mArthurID, new Vec3(-5, 0, 0));

        try {
            world.addModel(ObjParser.Parse(mContext, "models", "plancher.obj").get(0).toStaticModel());
        }catch(IOException ex){
            Log.e("Load Model", ex.getMessage(), ex);
        }
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    float time=0;
    @Override
    protected void onDrawFrame(World world) {
        world.Move(mArthurID, new Vec3(1f, 0, 0), getElapsedFrameTime());
        time += getElapsedFrameTime()/1000.f;
        if(time >= 2) {
         /*   try {
                mArthur.setSkin(R.drawable.arthur11_1);
            }catch(IOException ex){
                Log.e("Change Skin", ex.getMessage(), ex);
            }*/
        }
    }
}