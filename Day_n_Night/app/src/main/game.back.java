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

        try {
            StaticModel mur = ObjParser.Parse(mContext, "models", "mur.obj").get(0).toStaticModel();
            mur.setType(StaticModel.Type.WALL_TOP);
            world.addModel(mur);
        }catch(IOException ex){
            Log.e("Load Model", ex.getMessage(), ex);
        }

        mArthur = new Arthur(mContext);
        mArthur.getModel().StaticTranslate(new Vec3(-1, -2, 0));
        mArthur.setInWorldID(world.addModel(mArthur.getModel()));
        //world.Translate(mArthur.getInWorldID(), new Vec3(0, 4, 0));
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    float time=0;
    @Override
    protected void onDrawFrame(World world) {
        time += getElapsedFrameTime()/1000.f;
        if(time < 4) {
            world.Move(mArthur.getInWorldID(), new Vec3(0, 1, 0), getElapsedFrameTime());
        }else if(time >=4 && time <4.1){
            mArthur.getModel().setTranslation(new Vec3(-5, 2, 0));
         /*   try {
                mArthur.setSkin(R.drawable.arthur11_1);
            }catch(IOException ex){
                Log.e("Change Skin", ex.getMessage(), ex);
            }*/
        }else{
            world.Move(mArthur.getInWorldID(), new Vec3(1, 0, 0), getElapsedFrameTime());
        }
    }
}
